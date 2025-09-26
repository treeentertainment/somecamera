
package tech.treeentertainment.camera.ptp.commands.nikon;

import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.PtpAction;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Datatype;
import tech.treeentertainment.camera.ptp.PtpConstants.Property;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;
import tech.treeentertainment.camera.ptp.commands.CloseSessionCommand;
import tech.treeentertainment.camera.ptp.commands.SetDevicePropValueCommand;

public class NikonCloseSessionAction implements PtpAction {

    private final NikonCamera camera;

    public NikonCloseSessionAction(NikonCamera camera) {
        this.camera = camera;
    }

    @Override
    public void exec(IO io) {
        SetDevicePropValueCommand setRecordingMedia = new SetDevicePropValueCommand(camera,
                Property.NikonRecordingMedia, 0,
                Datatype.uint8);
        io.handleCommand(setRecordingMedia);

        if (setRecordingMedia.getResponseCode() == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        }

        io.handleCommand(new CloseSessionCommand(camera));
        camera.onSessionClosed();
    }

    @Override
    public void reset() {
    }
}
