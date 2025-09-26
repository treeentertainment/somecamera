
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.AppConfig;
import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;
import tech.treeentertainment.camera.ptp.model.ObjectInfo;

import android.util.Log;

public class GetObjectInfoCommand extends Command {

    private final String TAG = GetObjectInfoCommand.class.getSimpleName();

    private final int outObjectHandle;
    private ObjectInfo inObjectInfo;

    public GetObjectInfoCommand(PtpCamera camera, int objectHandle) {
        super(camera);
        this.outObjectHandle = objectHandle;
    }

    public ObjectInfo getObjectInfo() {
        return inObjectInfo;
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
        }
        if (inObjectInfo != null) {
            if (AppConfig.LOG) {
                Log.i(TAG, inObjectInfo.toString());
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        inObjectInfo = null;
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.GetObjectInfo, outObjectHandle);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        inObjectInfo = new ObjectInfo(b, length);
    }
}
