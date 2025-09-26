
package tech.treeentertainment.camera.ptp.commands.eos;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.EosCamera;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

public class EosSetPcModeCommand extends EosCommand {

    public EosSetPcModeCommand(EosCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode != Response.Ok) {
            camera.onPtpError(String.format("Couldn't initialize session! setting PC Mode failed, error code %s",
                    PtpConstants.responseToString(responseCode)));
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.EosSetPCConnectMode, 1);
    }
}
