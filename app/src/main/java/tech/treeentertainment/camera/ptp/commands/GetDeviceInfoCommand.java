
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;
import tech.treeentertainment.camera.ptp.model.DeviceInfo;

public class GetDeviceInfoCommand extends Command {

    private DeviceInfo info;

    public GetDeviceInfoCommand(PtpCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode != Response.Ok) {
            camera.onPtpError(String.format("Couldn't read device information, error code \"%s\"",
                    PtpConstants.responseToString(responseCode)));
        } else if (info == null) {
            camera.onPtpError("Couldn't retrieve device information");
        }
    }

    @Override
    public void reset() {
        super.reset();
        info = null;
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.GetDeviceInfo);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        info = new DeviceInfo(b, length);
        camera.setDeviceInfo(info);
    }
}
