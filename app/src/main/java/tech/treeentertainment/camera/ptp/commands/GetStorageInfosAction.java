
package tech.treeentertainment.camera.ptp.commands;

import tech.treeentertainment.camera.ptp.Camera;
import tech.treeentertainment.camera.ptp.Camera.StorageInfoListener;
import tech.treeentertainment.camera.ptp.PtpAction;
import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

public class GetStorageInfosAction implements PtpAction {

    private final PtpCamera camera;
    private final StorageInfoListener listener;

    public GetStorageInfosAction(PtpCamera camera, Camera.StorageInfoListener listener) {
        this.camera = camera;
        this.listener = listener;
    }

    @Override
    public void exec(IO io) {
        GetStorageIdsCommand getStorageIds = new GetStorageIdsCommand(camera);
        io.handleCommand(getStorageIds);

        if (getStorageIds.getResponseCode() != Response.Ok) {
            listener.onAllStoragesFound();
            return;
        }

        int ids[] = getStorageIds.getStorageIds();
        for (int i = 0; i < ids.length; ++i) {
            int storageId = ids[i];
            GetStorageInfoCommand c = new GetStorageInfoCommand(camera, storageId);
            io.handleCommand(c);

            if (c.getResponseCode() != Response.Ok) {
                listener.onAllStoragesFound();
                return;
            }

            String label = c.getStorageInfo().volumeLabel.isEmpty() ? c.getStorageInfo().storageDescription : c
                    .getStorageInfo().volumeLabel;
            if (label == null || label.isEmpty()) {
                label = "Storage " + i;
            }
            listener.onStorageFound(storageId, label);
        }

        listener.onAllStoragesFound();
    }

    @Override
    public void reset() {
    }
}
