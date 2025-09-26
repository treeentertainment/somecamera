
package tech.treeentertainment.camera.ptp;

public class FocusPoint {
    public int id;
    public float posx;
    public float posy;
    public float radius;

    public FocusPoint(int id, float posx, float posy, float radius) {
        this.id = id;
        this.posx = posx;
        this.posy = posy;
        this.radius = radius;
    }
}
