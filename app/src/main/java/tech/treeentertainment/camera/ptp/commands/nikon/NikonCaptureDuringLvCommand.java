package tech.treeentertainment.camera.ptp.commands.nikon;

import android.annotation.SuppressLint;
import android.util.Log;

import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.commands.InitiateCaptureCommand;
import tech.treeentertainment.camera.ptp.commands.SimpleCommand; // ✅ 추가
public class NikonCaptureDuringLvCommand extends SimpleCommand {

    public NikonCaptureDuringLvCommand(PtpCamera camera) {
        super(camera, 0x90C0); // Nikon vendor-specific CaptureDuringLv
    }

    @SuppressLint("LongLogTag")
    @Override
    public void exec(PtpCamera.IO io) {
        super.exec(io);
        int code = getResponseCode();
        Log.d("NikonCaptureDuringLvCommand",
                "Response = " + code + " (" + PtpConstants.responseToString(code) + ")");

        if (code == PtpConstants.Response.ParameterNotSupported) {
            Log.w("NikonCaptureDuringLvCommand",
                    "D700 does not support CaptureDuringLv. Falling back.");

            if (camera instanceof NikonCamera) {
                NikonCamera nikonCamera = (NikonCamera) camera;

                nikonCamera.queue.add(new NikonStopLiveViewAction(nikonCamera, false, () -> {
                    nikonCamera.enqueue(new InitiateCaptureCommand(nikonCamera), 500);

                    // LiveView 재개
                    nikonCamera.queue.add(new NikonStartLiveViewAction(nikonCamera));
                }));

            } else {
                Log.e("NikonCaptureDuringLvCommand", "Camera is not a NikonCamera, fallback skipped");
            }
        }
    }



}
