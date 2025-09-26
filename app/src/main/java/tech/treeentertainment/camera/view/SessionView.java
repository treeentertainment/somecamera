
package tech.treeentertainment.camera.view;

import android.graphics.Bitmap;

import tech.treeentertainment.camera.ptp.Camera;
import tech.treeentertainment.camera.ptp.model.LiveViewData;

public interface SessionView {

    public abstract void enableUi(boolean enabled);

    public abstract void cameraStarted(Camera camera);

    public abstract void cameraStopped(Camera camera);

    public abstract void propertyChanged(int property, int value);

    public abstract void propertyDescChanged(int property, int[] values);

    public abstract void setCaptureBtnText(String text);

    public abstract void focusStarted();

    public abstract void focusEnded(boolean hasFocused);

    public abstract void liveViewStarted();

    public abstract void liveViewStopped();

    public abstract void liveViewData(LiveViewData data);

    public abstract void capturedPictureReceived(int objectHandle, String filename, Bitmap thumbnail, Bitmap bitmap);

    public abstract void objectAdded(int handle, int format);
}