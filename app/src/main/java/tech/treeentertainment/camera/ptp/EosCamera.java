
package tech.treeentertainment.camera.ptp;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.commands.SimpleCommand;
import tech.treeentertainment.camera.ptp.commands.eos.EosEventCheckCommand;
import tech.treeentertainment.camera.ptp.commands.eos.EosGetLiveViewPictureCommand;
import tech.treeentertainment.camera.ptp.commands.eos.EosOpenSessionAction;
import tech.treeentertainment.camera.ptp.commands.eos.EosSetLiveViewAction;
import tech.treeentertainment.camera.ptp.commands.eos.EosSetPropertyCommand;
import tech.treeentertainment.camera.ptp.commands.eos.EosTakePictureCommand;
import tech.treeentertainment.camera.ptp.commands.nikon.NikonCaptureDuringLvCommand;
import tech.treeentertainment.camera.ptp.commands.nikon.NikonStopLiveViewAction;
import tech.treeentertainment.camera.ptp.commands.nikon.NikonTakePictureCommand;
import tech.treeentertainment.camera.ptp.model.LiveViewData;

public class EosCamera extends PtpCamera {

    public EosCamera(PtpUsbConnection connection, CameraListener listener, WorkerListener workerListener) {
        super(connection, listener, workerListener);

        addPropertyMapping(Camera.Property.ShutterSpeed, PtpConstants.Property.EosShutterSpeed);
        addPropertyMapping(Camera.Property.ApertureValue, PtpConstants.Property.EosApertureValue);
        addPropertyMapping(Camera.Property.IsoSpeed, PtpConstants.Property.EosIsoSpeed);
        addPropertyMapping(Camera.Property.Whitebalance, PtpConstants.Property.EosWhitebalance);
        addPropertyMapping(Camera.Property.ShootingMode, PtpConstants.Property.EosShootingMode);
        addPropertyMapping(Camera.Property.AvailableShots, PtpConstants.Property.EosAvailableShots);
        addPropertyMapping(Camera.Property.ColorTemperature, PtpConstants.Property.EosColorTemperature);
        addPropertyMapping(Camera.Property.FocusMode, PtpConstants.Property.EosAfMode);
        addPropertyMapping(Camera.Property.PictureStyle, PtpConstants.Property.EosPictureStyle);
        addPropertyMapping(Camera.Property.ExposureMeteringMode, PtpConstants.Property.EosMeteringMode);
        addPropertyMapping(Camera.Property.ExposureCompensation, PtpConstants.Property.EosExposureCompensation);

        histogramSupported = true;
    }

    @Override
    protected void onOperationCodesReceived(Set<Integer> operations) {
        if (operations.contains(Operation.EosGetLiveViewPicture)) {
            liveViewSupported = true;
        }
        if (operations.contains(Operation.EosBulbStart) && operations.contains(Operation.EosBulbEnd)) {
            bulbSupported = true;
        }
        if (operations.contains(Operation.EosDriveLens)) {
            driveLensSupported = true;
        }
        if (operations.contains(Operation.EosRemoteReleaseOn) && operations.contains(Operation.EosRemoteReleaseOff)) {
            //autoFocusSupported = true;
        }
    }

    public void onEventDirItemCreated(int objectHandle, int storageId, int objectFormat, String filename) {
        onEventObjectAdded(objectHandle, objectFormat);
    }

    @Override
    protected void openSession() {
        queue.add(new EosOpenSessionAction(this));
    }

    @Override
    protected void queueEventCheck() {
        queue.add(new EosEventCheckCommand(this));
    }

    @Override
    public void focus() {
        //queue.add(new SimpleCommand(this, Operation.EosRemoteReleaseOn, 1, 0));
    }

    @Override
    public void capture(int mode) {
        if (mode == CAPTURE_HIGH_QUALITY) {
                if (isBulbCurrentShutterSpeed()) {
                    queue.add(new SimpleCommand(this, cameraIsCapturing ? Operation.EosBulbEnd : Operation.EosBulbStart));
                } else {
                    queue.add(new EosTakePictureCommand(this));
                }
        } else {
            // LiveView 프레임 캡쳐
            LiveViewData frame = new LiveViewData();
            frame.setFrameReadyListener(bmp -> {

                if (bmp != null) {
                    Log.d("CameraCapture", "LiveView frame captured, size="
                            + bmp.getWidth() + "x" + bmp.getHeight());

                    // 1. mutable 복사
                    bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
                    Log.d("CameraCapture", "Bitmap copied as mutable");

                    saveBitmapToPhone(bmp, "liveview_" + System.currentTimeMillis() + ".jpg");

                    // 3. Fragment listener 호출
                    if (listener != null) {
                        int handle = (int) System.currentTimeMillis();
                        Log.d("CameraCapture", "Notify listener: handle=" + handle);
                        listener.onCapturedPictureReceived(
                                handle,
                                "liveview_" + handle + ".jpg",
                                bmp,
                                bmp
                        );
                    } else {
                        Log.w("CameraCapture", "Listener is null, cannot deliver bitmap");
                    }
                } else {
                    Log.w("CameraCapture", "LiveView frame capture failed, bmp=null");
                }
            });
            getLiveViewPicture(frame);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveBitmapToPhone(Bitmap bmp, String fileName) {
        try {
            // 저장 경로: Pictures 디렉토리 사용 (API 29+ 대응)
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "somecamera");
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, fileName);
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 95, out);
            out.flush();
            out.close();
            Log.d("CameraCapture", "Bitmap saved: " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e("CameraCapture", "Bitmap save failed", e);
        }
    }

    @Override
    public void setProperty(int property, int value) {
        if (properties.containsKey(property)) {
            queue.add(new EosSetPropertyCommand(this, virtualToPtpProperty.get(property), value));
        }
    }

    @Override
    public void setLiveView(boolean enabled) {
        if (liveViewSupported) {
            queue.add(new EosSetLiveViewAction(this, enabled));
        }
    }

    @Override
    public void getLiveViewPicture(LiveViewData data) {
        if (liveViewOpen) {
            queue.add(new EosGetLiveViewPictureCommand(this, data));
        } else {
            Log.w("EosCamera", "LiveView is not open, cannot capture frame");
        }
    }


    @Override
    protected boolean isBulbCurrentShutterSpeed() {
        Integer value = ptpProperties.get(PtpConstants.Property.EosShutterSpeed);
        return bulbSupported && value != null && value == PtpPropertyHelper.EOS_SHUTTER_SPEED_BULB;
    }

    @Override
    public void driveLens(int driveDirection, int pulses) {
        if (driveLensSupported && liveViewOpen) {
            int value = driveDirection == Camera.DriveLens.Near ? 0 : 0x8000;
            switch (pulses) {
            case DriveLens.Hard:
                value |= 0x0003;
                break;
            case DriveLens.Medium:
                value |= 0x0002;
                break;
            case DriveLens.Soft:
            default:
                value |= 0x0001;
                break;
            }
            queue.add(new SimpleCommand(this, PtpConstants.Operation.EosDriveLens, value));
        }
    }

    @Override
    public boolean isSettingPropertyPossible(int property) {
        Integer mode = ptpProperties.get(PtpConstants.Property.EosShootingMode);
        Integer wb = ptpProperties.get(PtpConstants.Property.WhiteBalance);
        if (mode == null) {
            return false;
        }
        switch (property) {
        case Property.ShutterSpeed:
            return mode == 3 || mode == 1;
        case Property.ApertureValue:
            return mode == 3 || mode == 2;
        case Property.IsoSpeed:
        case Property.Whitebalance:
        case Property.ExposureMeteringMode:
            return mode >= 0 && mode <= 6;
        case Property.FocusPoints:
            return false;
        case Property.ExposureCompensation:
            return mode == 0 || mode == 1 || mode == 2 || mode == 5 || mode == 6;
        case Property.ColorTemperature:
            return wb != null && wb == 9;
        default:
            return true;
        }
    }

    @Override
    public void setLiveViewAfArea(float posx, float posy) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<FocusPoint> getFocusPoints() {
        return new ArrayList<FocusPoint>();
    }
}
