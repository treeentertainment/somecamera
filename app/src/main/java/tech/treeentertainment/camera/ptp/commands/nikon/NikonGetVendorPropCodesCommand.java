
package tech.treeentertainment.camera.ptp.commands.nikon;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.PacketUtil;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;

public class NikonGetVendorPropCodesCommand extends NikonCommand {

    private int[] propertyCodes = new int[0];

    public NikonGetVendorPropCodesCommand(NikonCamera camera) {
        super(camera);
    }

    public int[] getPropertyCodes() {
        return propertyCodes;
    }

    @Override
    public void exec(IO io) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonGetVendorPropCodes);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        propertyCodes = PacketUtil.readU16Array(b);
    }
}
