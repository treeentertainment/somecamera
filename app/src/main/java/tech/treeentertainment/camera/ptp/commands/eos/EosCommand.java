package tech.treeentertainment.camera.ptp.commands.eos;

import tech.treeentertainment.camera.ptp.EosCamera;
import tech.treeentertainment.camera.ptp.commands.Command;

public abstract class EosCommand extends Command {

    protected EosCamera camera;

    public EosCommand(EosCamera camera) {
        super(camera);
        this.camera = camera;
    }
}
