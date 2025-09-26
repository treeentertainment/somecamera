
package tech.treeentertainment.camera.ptp.commands.nikon;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

public class NikonAfDriveDeviceReadyCommand extends NikonCommand {

    public NikonAfDriveDeviceReadyCommand(NikonCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() == Response.DeviceBusy) {
            reset();
            camera.enqueue(this, 200);
        } else {
            camera.onFocusEnded(getResponseCode() == Response.Ok);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonDeviceReady);
    }
}
