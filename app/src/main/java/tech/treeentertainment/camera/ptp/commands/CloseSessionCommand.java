
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.AppConfig;
import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

import android.util.Log;

public class CloseSessionCommand extends Command {

    private final String TAG = CloseSessionCommand.class.getSimpleName();

    public CloseSessionCommand(PtpCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        // Can this even happen?
        if (responseCode == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        }
        // close even when error happened
        camera.onSessionClosed();
        if (responseCode != Response.Ok) {
            // TODO error report
            if (AppConfig.LOG) {
                Log.w(TAG,
                        String.format("Error response when closing session, response %s",
                                PtpConstants.responseToString(responseCode)));
            }
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.CloseSession);
    }
}
