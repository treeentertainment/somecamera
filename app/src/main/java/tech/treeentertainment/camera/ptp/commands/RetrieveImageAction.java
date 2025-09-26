
package tech.treeentertainment.camera.ptp.commands;

import tech.treeentertainment.camera.ptp.Camera.RetrieveImageListener;
import tech.treeentertainment.camera.ptp.PtpAction;
import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

public class RetrieveImageAction implements PtpAction {

    private final PtpCamera camera;
    private final int objectHandle;
    private final RetrieveImageListener listener;
    private final int sampleSize;

    public RetrieveImageAction(PtpCamera camera, RetrieveImageListener listener, int objectHandle, int sampleSize) {
        this.camera = camera;
        this.listener = listener;
        this.objectHandle = objectHandle;
        this.sampleSize = sampleSize;
    }

    @Override
    public void exec(IO io) {
        GetObjectCommand getObject = new GetObjectCommand(camera, objectHandle, sampleSize);
        io.handleCommand(getObject);

        if (getObject.getResponseCode() != Response.Ok || getObject.getBitmap() == null) {
            listener.onImageRetrieved(0, null);
            return;
        }

        listener.onImageRetrieved(objectHandle, getObject.getBitmap());
    }

    @Override
    public void reset() {
    }
}
