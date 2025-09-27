
package tech.treeentertainment.camera.ptp.commands.eos;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.graphics.Bitmap;
import tech.treeentertainment.camera.AppConfig;
import tech.treeentertainment.camera.ptp.EosCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;
import tech.treeentertainment.camera.ptp.model.LiveViewData;

public class EosGetLiveViewPictureCommand extends EosCommand {

    private static final String TAG = EosGetLiveViewPictureCommand.class.getSimpleName();
    private static byte[] tmpStorage = new byte[0x4000];
    private final Options options;
    private LiveViewData data;

    public EosGetLiveViewPictureCommand(EosCamera camera, LiveViewData data) {
        super(camera);
        if (data == null) {
            this.data = new LiveViewData();
            this.data.histogram = ByteBuffer.allocate(1024 * 4);
            this.data.histogram.order(ByteOrder.LITTLE_ENDIAN);
        } else {
            this.data = data;
        }
        options = new BitmapFactory.Options();
//        options.inBitmap = this.data.bitmap;
        options.inSampleSize = 1;
        options.inTempStorage = tmpStorage;
//        this.data.bitmap = null;
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        }
        if (this.data.getBitmap() != null && responseCode == Response.Ok) {
            camera.onLiveViewReceived(data);
        } else {
            camera.onLiveViewReceived(null);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.EosGetLiveViewPicture, 0x100000);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        Log.d(TAG, "decodeData() called, length=" + length);

        data.hasHistogram = false;
        data.hasAfFrame = false;

        if (length < 1000) {
            if (AppConfig.LOG) {
                Log.w(TAG, String.format("liveview data size too small %d", length));
            }
            return;
        }

        try {

            while (b.hasRemaining()) {
                int subLength = b.getInt();
                int type = b.getInt();

                if (subLength < 8) {
                    throw new RuntimeException("Invalid sub size " + subLength);
                }

                int unknownInt = 0;

                switch (type) {
                    case 0x01:
                        byte[] jpegData = new byte[subLength - 8];
                        b.get(jpegData);

                        int soi = findMarker(jpegData, (byte)0xFF, (byte)0xD8);
                        int eoi = findMarker(jpegData, (byte)0xFF, (byte)0xD9);

                        Log.d(TAG, "subBlock: type=" + String.format("0x%02X", type)
                                + ", subLength=" + subLength
                                + ", pos=" + b.position());

                        if (soi >= 0 && eoi > soi) {
                            int jpegLen = eoi - soi + 2;
                            Log.d(TAG, "decodeData: soi=" + soi + ", eoi=" + eoi + ", jpegLen=" + jpegLen);

                            Bitmap bitmap = BitmapFactory.decodeByteArray(jpegData, soi, jpegLen, options);
                            if (bitmap != null && !bitmap.isMutable()) {
                                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                            }
                            data.setBitmap(bitmap);
                        } else {
                            Log.w(TAG, "JPEG markers not found in EOS sub-block");
                        }
                        break;
                case 0x04:
                    data.zoomFactor = b.getInt();
                    break;
                case 0x05:
                    // zoomfocusx, zoomfocusy
                    data.zoomRectRight = b.getInt();
                    data.zoomRectBottom = b.getInt();
                    if (AppConfig.LOG) {
                        Log.i(TAG, "header 5 " + data.zoomRectRight + " " + data.zoomRectBottom);
                    }
                    break;
                case 0x06:
                    // imagex, imagey (if zoomed should be non zero)
                    data.zoomRectLeft = b.getInt();
                    data.zoomRectTop = b.getInt();
                    if (AppConfig.LOG) {
                        Log.i(TAG, "header 6 " + data.zoomRectLeft + " " + data.zoomRectTop);
                    }
                    break;
                case 0x07:
                    unknownInt = b.getInt();
                    if (AppConfig.LOG) {
                        Log.i(TAG, "header 7 " + unknownInt + " " + subLength);
                    }
                    break;
                case 0x03:
                    data.hasHistogram = true;
                    b.get(data.histogram.array(), 0, 1024 * 4);
                    break;
                case 0x08: // faces if faces focus
                case 0x0e: // TODO original width, original height
                default:
                    b.position(b.position() + subLength - 8);
                    if (AppConfig.LOG) {
                        Log.i(TAG, "unknown header " + type + " size " + subLength);
                    }
                    break;
                }

                if (length - b.position() < 8) {
                    break;
                }
            }


        } catch (RuntimeException e) {
            Log.e(TAG, "" + e.toString());
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
    }

    /**
     * JPEG 마커(SOI=FFD8, EOI=FFD9) 검색
     *
     * @param data   검색할 바이트 배열
     * @param b1     첫 번째 바이트 (0xFF)
     * @param b2     두 번째 바이트 (0xD8 또는 0xD9)
     * @return 발견된 인덱스 (없으면 -1)
     */
    private int findMarker(byte[] data, byte b1, byte b2) {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] == b1 && data[i + 1] == b2) {
                return i;
            }
        }
        return -1;
    }

}
