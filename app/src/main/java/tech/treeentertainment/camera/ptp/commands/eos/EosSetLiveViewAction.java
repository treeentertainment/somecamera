
package tech.treeentertainment.camera.ptp.commands.eos;

import tech.treeentertainment.camera.ptp.EosCamera;
import tech.treeentertainment.camera.ptp.EosConstants;
import tech.treeentertainment.camera.ptp.PtpAction;
import tech.treeentertainment.camera.ptp.EosConstants.EvfMode;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Property;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

public class EosSetLiveViewAction implements PtpAction {

    private final EosCamera camera;
    private final boolean enabled;

    public EosSetLiveViewAction(EosCamera camera, boolean enabled) {
        this.camera = camera;
        this.enabled = enabled;
    }

    @Override
    public void exec(IO io) {
        int evfMode = camera.getPtpProperty(Property.EosEvfMode);

        if (enabled && evfMode != EvfMode.ENABLE || !enabled && evfMode != EvfMode.DISABLE) {
            EosSetPropertyCommand setEvfMode = new EosSetPropertyCommand(camera, Property.EosEvfMode,
                    enabled ? EvfMode.ENABLE : EvfMode.DISABLE);
            io.handleCommand(setEvfMode);

            if (setEvfMode.getResponseCode() == Response.DeviceBusy) {
                camera.onDeviceBusy(this, true);
                return;
            } else if (setEvfMode.getResponseCode() != Response.Ok) {
                camera.onPtpWarning("Couldn't open live view");
                return;
            }
        }

        int outputDevice = camera.getPtpProperty(Property.EosEvfOutputDevice);

        if (enabled) {
            outputDevice |= EosConstants.EvfOutputDevice.PC;
        } else {
            outputDevice &= ~EosConstants.EvfOutputDevice.PC;
        }

        EosSetPropertyCommand setOutputDevice = new EosSetPropertyCommand(camera, Property.EosEvfOutputDevice,
                outputDevice);
        io.handleCommand(setOutputDevice);

        if (setOutputDevice.getResponseCode() == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
        } else if (setOutputDevice.getResponseCode() == Response.Ok) {
            if (!enabled) {
                camera.onLiveViewStopped();
            } else {
                camera.onLiveViewStarted();
            }
            return;
        } else {
            camera.onPtpWarning("Couldn't open live view");
        }

    }

    @Override
    public void reset() {
    }
}
