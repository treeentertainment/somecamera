
package tech.treeentertainment.camera.ptp.commands.nikon;

import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.PtpAction;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;
import tech.treeentertainment.camera.ptp.commands.SimpleCommand;

public class NikonStopLiveViewAction implements PtpAction {
    private final NikonCamera camera;
    private final boolean notifyUser;
    private final Runnable callback;

    public NikonStopLiveViewAction(NikonCamera camera, boolean notifyUser, Runnable callback) {
        this.camera = camera;
        this.notifyUser = notifyUser;
        this.callback = callback;
    }

    @Override
    public void exec(IO io) {
        SimpleCommand simpleCmd = new SimpleCommand(camera, Operation.NikonEndLiveView);
        io.handleCommand(simpleCmd);

        if (simpleCmd.getResponseCode() == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
        } else {
            if (notifyUser) {
                camera.onLiveViewStopped();
            } else {
                camera.onLiveViewStoppedInternal();
            }

            if (callback != null) {
                callback.run();
            }
        }
    }

    @Override
    public void reset() {
    }
}