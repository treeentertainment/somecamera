
package tech.treeentertainment.camera.ptp.commands.nikon;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

public class NikonAfDriveCommand extends NikonCommand {

    public NikonAfDriveCommand(NikonCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        //        if (camera.isInActivationTypePhase()) {
        //            return;
        //        }
        io.handleCommand(this);
        if (getResponseCode() == Response.Ok) {
            camera.onFocusStarted();
            camera.enqueue(new NikonAfDriveDeviceReadyCommand(camera), 200);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonAfDrive);
    }
}
