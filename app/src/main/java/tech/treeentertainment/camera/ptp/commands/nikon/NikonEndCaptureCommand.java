package tech.treeentertainment.camera.ptp.commands.nikon;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.commands.Command;

public class NikonEndCaptureCommand extends Command {
    public static final int NIKON_END_CAPTURE = 0x90C1;

    public NikonEndCaptureCommand(PtpCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, NIKON_END_CAPTURE, 0, 0);
    }
}
