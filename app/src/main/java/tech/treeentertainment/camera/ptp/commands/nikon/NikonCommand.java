package tech.treeentertainment.camera.ptp.commands.nikon;

import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.commands.Command;

public abstract class NikonCommand extends Command {

    protected NikonCamera camera;

    public NikonCommand(NikonCamera camera) {
        super(camera);
        this.camera = camera;
    }
}
