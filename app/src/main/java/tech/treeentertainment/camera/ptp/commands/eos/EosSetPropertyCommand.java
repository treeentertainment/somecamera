
package tech.treeentertainment.camera.ptp.commands.eos;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.EosCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;
import tech.treeentertainment.camera.ptp.PtpConstants.Type;

public class EosSetPropertyCommand extends EosCommand {

    private final int property;
    private final int value;

    public EosSetPropertyCommand(EosCamera camera, int property, int value) {
        super(camera);
        hasDataToSend = true;
        this.property = property;
        this.value = value;
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.EosSetDevicePropValue);
    }

    @Override
    public void encodeData(ByteBuffer b) {
        // header
        b.putInt(24);
        b.putShort((short) Type.Data);
        b.putShort((short) Operation.EosSetDevicePropValue);
        b.putInt(camera.currentTransactionId());
        // specific block
        b.putInt(0x0C);
        b.putInt(property);
        b.putInt(value);
    }
}
