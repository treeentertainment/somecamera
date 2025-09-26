
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.PtpConstants.Datatype;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;
import tech.treeentertainment.camera.ptp.PtpConstants.Type;

public class SetDevicePropValueCommand extends Command {

    private final int property;
    private final int value;
    private final int datatype;

    public SetDevicePropValueCommand(PtpCamera camera, int property, int value, int datatype) {
        super(camera);
        this.property = property;
        this.value = value;
        this.datatype = datatype;
        hasDataToSend = true;
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        } else if (responseCode == Response.Ok) {
            camera.onPropertyChanged(property, value);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.SetDevicePropValue, property);
    }

    @Override
    public void encodeData(ByteBuffer b) {
        // header
        b.putInt(12 + PtpConstants.getDatatypeSize(datatype));
        b.putShort((short) Type.Data);
        b.putShort((short) Operation.SetDevicePropValue);
        b.putInt(camera.currentTransactionId());
        // specific block
        if (datatype == Datatype.int8 || datatype == Datatype.uint8) {
            b.put((byte) value);
        } else if (datatype == Datatype.int16 || datatype == Datatype.uint16) {
            b.putShort((short) value);
        } else if (datatype == Datatype.int32 || datatype == Datatype.uint32) {
            b.putInt(value);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
