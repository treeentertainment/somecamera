
package tech.treeentertainment.camera.ptp.commands;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;

public class SimpleCommand extends Command {

    private final int operation;
    private int numParams;
    private int p0;
    private int p1;

    public SimpleCommand(PtpCamera camera, int operation) {
        super(camera);
        this.operation = operation;
    }

    public SimpleCommand(PtpCamera camera, int operation, int p0) {
        super(camera);
        this.operation = operation;
        this.p0 = p0;
        this.numParams = 1;
    }

    public SimpleCommand(PtpCamera camera, int operation, int p0, int p1) {
        super(camera);
        this.operation = operation;
        this.p0 = p0;
        this.p1 = p1;
        this.numParams = 2;
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
            return;
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        if (numParams == 2) {
            encodeCommand(b, operation, p0, p1);
        } else if (numParams == 1) {
            encodeCommand(b, operation, p0);
        } else {
            encodeCommand(b, operation);
        }
    }
}
