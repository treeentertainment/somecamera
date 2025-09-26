
package tech.treeentertainment.camera.view;

import tech.treeentertainment.camera.AppSettings;
import tech.treeentertainment.camera.ptp.Camera;

public abstract class SessionFragment extends BaseFragment implements SessionView {

    protected boolean inStart;

    protected Camera camera() {
        if (getActivity() instanceof SessionActivity) {
            return ((SessionActivity) getActivity()).getCamera();
        }
        return null;
    }

    protected AppSettings getSettings() {
        return ((SessionActivity) getActivity()).getSettings();
    }

    @Override
    public void onStart() {
        super.onStart();
        inStart = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        inStart = false;
    }
}
