package tech.treeentertainment.camera.ptp;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import android.os.Build;

import tech.treeentertainment.camera.AppConfig;
import tech.treeentertainment.camera.ptp.Camera.CameraListener;
import tech.treeentertainment.camera.ptp.PtpCamera.State;

public class PtpUsbService implements PtpService {

    private final String TAG = PtpUsbService.class.getSimpleName();

    private static final String ACTION_USB_PERMISSION = "tech.treeentertainment.camera.USB_PERMISSION";
    private final BroadcastReceiver permissionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
                synchronized (this) {
                    UsbDevice device;
                    if (Build.VERSION.SDK_INT >= 33) {
                        device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice.class);
                    } else {
                        @SuppressWarnings("deprecation")
                        UsbDevice legacy = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        device = legacy;
                    }

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        connect(context, device);
                    } else {
                        // 권한 거부됨
                    }
                }
            }
        }
    };

    private final Handler handler = new Handler();
    private final UsbManager usbManager;
    private PtpCamera camera;
    private CameraListener listener;

    Runnable shutdownRunnable = () -> shutdown();

    public PtpUsbService(Context context) {
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    @Override
    public void setCameraListener(CameraListener listener) {
        this.listener = listener;
        if (camera != null) {
            camera.setListener(listener);
        }
    }

    @Override
    public void initialize(Context context, Intent intent) {
        handler.removeCallbacks(shutdownRunnable);
        if (camera != null) {
            if (AppConfig.LOG) {
                Log.i(TAG, "initialize: camera available");
            }
            if (camera.getState() == State.Active) {
                if (listener != null) {
                    listener.onCameraStarted(camera);
                }
                return;
            }
            if (AppConfig.LOG) {
                Log.i(TAG, "initialize: camera not active, satet " + camera.getState());
            }
            camera.shutdownHard();
        }
        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        if (device != null) {
            if (AppConfig.LOG) {
                Log.i(TAG, "initialize: got device through intent");
            }
            connect(context, device);
        } else {
            if (AppConfig.LOG) {
                Log.i(TAG, "initialize: looking for compatible camera");
            }
            device = lookupCompatibleDevice(usbManager);
            if (device != null) {
                registerPermissionReceiver(context);
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        new Intent(ACTION_USB_PERMISSION),
                        PendingIntent.FLAG_IMMUTABLE  // ✅ Android 12+ 대응
                );
                usbManager.requestPermission(device, mPermissionIntent);
            } else {
                listener.onNoCameraFound();
            }
        }
    }

    @Override
    public void shutdown() {
        if (AppConfig.LOG) {
            Log.i(TAG, "shutdown");
        }
        if (camera != null) {
            camera.shutdown();
            camera = null;
        }
    }

    @Override
    public void lazyShutdown() {
        if (AppConfig.LOG) {
            Log.i(TAG, "lazy shutdown");
        }
        handler.postDelayed(shutdownRunnable, 4000);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void registerPermissionReceiver(Context context) {
        if (AppConfig.LOG) {
            Log.i(TAG, "register permission receiver");
        }
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 앱 내부에서만 받는 브로드캐스트이므로 NOT_EXPORTED 사용
            context.registerReceiver(permissionReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            context.registerReceiver(permissionReceiver, filter);
        }
    }

    private void unregisterPermissionReceiver(Context context) {
        if (AppConfig.LOG) {
            Log.i(TAG, "unregister permission receiver");
        }
        context.unregisterReceiver(permissionReceiver);
    }

    private UsbDevice lookupCompatibleDevice(UsbManager manager) {
        Map<String, UsbDevice> deviceList = manager.getDeviceList();
        for (Map.Entry<String, UsbDevice> e : deviceList.entrySet()) {
            UsbDevice d = e.getValue();
            if (d.getVendorId() == PtpConstants.CanonVendorId || d.getVendorId() == PtpConstants.NikonVendorId) {
                return d;
            }
        }
        return null;
    }

    private boolean connect(Context context, UsbDevice device) {
        if (camera != null) {
            camera.shutdown();
            camera = null;
        }
        for (int i = 0, n = device.getInterfaceCount(); i < n; ++i) {
            UsbInterface intf = device.getInterface(i);

            if (intf.getEndpointCount() != 3) {
                continue;
            }

            UsbEndpoint in = null;
            UsbEndpoint out = null;

            for (int e = 0, en = intf.getEndpointCount(); e < en; ++e) {
                UsbEndpoint endpoint = intf.getEndpoint(e);
                if (endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                    if (endpoint.getDirection() == UsbConstants.USB_DIR_IN) {
                        in = endpoint;
                    } else if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
                        out = endpoint;
                    }
                }
            }

            if (in == null || out == null) {
                continue;
            }

            if (AppConfig.LOG) {
                Log.i(TAG, "Found compatible USB interface");
                Log.i(TAG, "Interface class " + intf.getInterfaceClass());
                Log.i(TAG, "Interface subclass " + intf.getInterfaceSubclass());
                Log.i(TAG, "Interface protocol " + intf.getInterfaceProtocol());
                Log.i(TAG, "Bulk out max size " + out.getMaxPacketSize());
                Log.i(TAG, "Bulk in max size " + in.getMaxPacketSize());
            }

            if (device.getVendorId() == PtpConstants.CanonVendorId) {
                PtpUsbConnection connection = new PtpUsbConnection(usbManager.openDevice(device), in, out,
                        device.getVendorId(), device.getProductId());
                camera = new EosCamera(connection, listener, new WorkerNotifier(context));
            } else if (device.getVendorId() == PtpConstants.NikonVendorId) {
                PtpUsbConnection connection = new PtpUsbConnection(usbManager.openDevice(device), in, out,
                        device.getVendorId(), device.getProductId());
                camera = new NikonCamera(connection, listener, new WorkerNotifier(context));
            }

            return true;
        }

        if (listener != null) {
            listener.onError("No compatible camera found");
        }

        return false;
    }
}
