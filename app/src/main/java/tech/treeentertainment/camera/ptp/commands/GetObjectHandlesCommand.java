
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.Camera.StorageInfoListener;
import tech.treeentertainment.camera.ptp.PacketUtil;
import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

public class GetObjectHandlesCommand extends Command {

    private final int storageId;
    private final int objectFormat;
    private final int associationHandle;
    private int[] objectHandles;
    private final StorageInfoListener listener;

    public int[] getObjectHandles() {
        if (objectHandles == null) {
            return new int[0];
        }
        return objectHandles;
    }

    public GetObjectHandlesCommand(PtpCamera camera, StorageInfoListener listener, int storageId) {
        this(camera, listener, storageId, 0, 0);
    }

    public GetObjectHandlesCommand(PtpCamera camera, StorageInfoListener listener, int storageId, int objectFormat) {
        this(camera, listener, storageId, objectFormat, 0);
    }

    public GetObjectHandlesCommand(PtpCamera camera, StorageInfoListener listener, int storageId, int objectFormat,
            int associationHandle) {
        super(camera);
        this.listener = listener;
        this.storageId = storageId;
        this.objectFormat = objectFormat;
        this.associationHandle = associationHandle;
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (getResponseCode() != Response.Ok) {
            // error
            listener.onImageHandlesRetrieved(new int[0]);
            return;
        }
        listener.onImageHandlesRetrieved(objectHandles);
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        super.encodeCommand(b, PtpConstants.Operation.GetObjectHandles, storageId, objectFormat, associationHandle);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        objectHandles = PacketUtil.readU32Array(b);
    }
}
