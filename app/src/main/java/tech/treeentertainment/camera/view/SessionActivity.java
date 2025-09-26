
package tech.treeentertainment.camera.view;

import androidx.appcompat.app.AppCompatActivity;

import tech.treeentertainment.camera.AppSettings;
import tech.treeentertainment.camera.ptp.Camera;

public abstract class SessionActivity extends AppCompatActivity  {

    public abstract Camera getCamera();

    public abstract void setSessionView(SessionView view);

    public abstract AppSettings getSettings();
}
