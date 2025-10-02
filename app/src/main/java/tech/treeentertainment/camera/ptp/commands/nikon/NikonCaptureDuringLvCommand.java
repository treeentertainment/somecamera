package tech.treeentertainment.camera.ptp.commands.nikon;

import android.annotation.SuppressLint;
import android.util.Log;

import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.commands.SimpleCommand; // ✅ 추가
public class NikonCaptureDuringLvCommand extends SimpleCommand {

    public NikonCaptureDuringLvCommand(PtpCamera camera) {
        super(camera, 0x9201); // Nikon vendor-specific CaptureDuringLv
    }

    @SuppressLint("LongLogTag")
    @Override
    public void exec(PtpCamera.IO io) {
        super.exec(io);
        int code = getResponseCode();
        Log.d("NikonCaptureDuringLvCommand",
                "Response = " + code + " (" + PtpConstants.responseToString(code) + ")");

        if (code == PtpConstants.Response.ParameterNotSupported || code == PtpConstants.Response.OperationNotSupported || code == PtpConstants.Response.DeviceBusy) {
            Log.w("NikonCaptureDuringLvCommand",
                    "D700 does not support CaptureDuringLv. Falling back.");

            if (camera instanceof NikonCamera) {
                NikonCamera nikonCamera = (NikonCamera) camera;

                nikonCamera.queue.add(new NikonStopLiveViewAction(nikonCamera, false, () -> {
                    android.util.Log.i("NikonCamera", "StopLiveView requested, waiting for event...");
                    nikonCamera.enqueue(new NikonTakePictureCommand(nikonCamera), 500);
                }));

            } else {
                Log.e("NikonCaptureDuringLvCommand", "Camera is not a NikonCamera, fallback skipped");
            }
        }
    }
}
