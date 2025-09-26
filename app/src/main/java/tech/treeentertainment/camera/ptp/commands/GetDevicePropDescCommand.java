
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.model.DevicePropDesc;

public class GetDevicePropDescCommand extends Command {

    private final int property;
    private DevicePropDesc devicePropDesc;

    public GetDevicePropDescCommand(PtpCamera camera, int property) {
        super(camera);
        this.property = property;
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode == PtpConstants.Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
        }
        if (devicePropDesc != null) {
            // this order is important
            camera.onPropertyDescChanged(property, devicePropDesc);
            camera.onPropertyChanged(property, devicePropDesc.currentValue);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, PtpConstants.Operation.GetDevicePropDesc, property);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        devicePropDesc = new DevicePropDesc(b, length);
    }
}
