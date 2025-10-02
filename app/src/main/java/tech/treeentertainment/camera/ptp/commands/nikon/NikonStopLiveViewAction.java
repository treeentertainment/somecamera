
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
        android.util.Log.i("NikonStopLiveViewAction", "exec() called, sending EndLiveView command...");
        SimpleCommand simpleCmd = new SimpleCommand(camera, Operation.NikonEndLiveView);
        io.handleCommand(simpleCmd);

        if (simpleCmd.getResponseCode() == Response.DeviceBusy) {
            android.util.Log.i("NikonStopLiveViewAction", "device busy");
            camera.onDeviceBusy(this, true);
        } else if (simpleCmd.getResponseCode() == Response.Ok) {
            if (notifyUser) {
                android.util.Log.i("NikonStopLiveViewAction", "notify");
                camera.onLiveViewStopped();
            } else {
                android.util.Log.i("NikonStopLiveViewAction", "internal");
                camera.onLiveViewStoppedInternal();
            }



            if (callback != null) {
                android.util.Log.i("NikonStopLiveViewAction", "callback run");
                callback.run();
            }
        } else {
            // 실패 로그 추가
            android.util.Log.w("NikonStopLiveViewAction",
                    "Failed to stop LiveView. Response=0x" + Integer.toHexString(simpleCmd.getResponseCode()));
        }
    }

    @Override
    public void reset() {
    }
}