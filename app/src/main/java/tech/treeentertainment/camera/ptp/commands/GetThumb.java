
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants;

public class GetThumb extends Command {

    private static final String TAG = GetThumb.class.getSimpleName();

    private final int objectHandle;
    private Bitmap inBitmap;

    public GetThumb(PtpCamera camera, int objectHandle) {
        super(camera);
        this.objectHandle = objectHandle;
    }

    public Bitmap getBitmap() {
        return inBitmap;
    }

    @Override
    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        super.reset();
        inBitmap = null;
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.GetThumb, objectHandle);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        try {
            // 12 == offset of data header
            inBitmap = BitmapFactory.decodeByteArray(b.array(), 12, length - 12);
        } catch (RuntimeException e) {
            Log.i(TAG, "exception on decoding picture : " + e.toString());
        } catch (OutOfMemoryError e) {
            System.gc();
        }
    }
}
