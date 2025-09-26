
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PacketUtil;
import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants;

public class GetStorageIdsCommand extends Command {

    private int[] storageIds;

    public int[] getStorageIds() {
        if (storageIds == null) {
            return new int[0];
        }
        return storageIds;
    }

    public GetStorageIdsCommand(PtpCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        super.encodeCommand(b, PtpConstants.Operation.GetStorageIDs);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        storageIds = PacketUtil.readU32Array(b);
    }
}
