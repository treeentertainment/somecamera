
package tech.treeentertainment.camera.ptp.commands.eos;

import tech.treeentertainment.camera.ptp.EosCamera;
import tech.treeentertainment.camera.ptp.PtpAction;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.commands.OpenSessionCommand;

public class EosOpenSessionAction implements PtpAction {

    private final EosCamera camera;

    public EosOpenSessionAction(EosCamera camera) {
        this.camera = camera;
    }

    @Override
    public void exec(IO io) {
        OpenSessionCommand openSession = new OpenSessionCommand(camera);
        io.handleCommand(openSession);
        if (openSession.getResponseCode() == PtpConstants.Response.Ok) {
            EosSetPcModeCommand setPcMode = new EosSetPcModeCommand(camera);
            io.handleCommand(setPcMode);
            if (setPcMode.getResponseCode() == PtpConstants.Response.Ok) {
                EosSetExtendedEventInfoCommand c = new EosSetExtendedEventInfoCommand(camera);
                io.handleCommand(c);
                if (c.getResponseCode() == PtpConstants.Response.Ok) {
                    camera.onSessionOpened();
                    return;
                } else {
                    camera.onPtpError(String.format(
                            "Couldn't open session! Setting extended event info failed with error code \"%s\"",
                            PtpConstants.responseToString(c.getResponseCode())));
                }
            } else {
                camera.onPtpError(String.format(
                        "Couldn't open session! Setting PcMode property failed with error code \"%s\"",
                        PtpConstants.responseToString(setPcMode.getResponseCode())));
            }
        } else {
            camera.onPtpError(String.format(
                    "Couldn't open session! Open session command failed with error code \"%s\"",
                    PtpConstants.responseToString(openSession.getResponseCode())));
        }
    }

    @Override
    public void reset() {
    }
}
