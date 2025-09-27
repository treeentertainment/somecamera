package tech.treeentertainment.camera.ptp.commands.nikon;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PtpCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants.Response;
import tech.treeentertainment.camera.ptp.commands.Command;

public class NikonTakePictureCommand extends Command {
    public static final int NIKON_START_CAPTURE = 0x90C0;

    public NikonTakePictureCommand(PtpCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
        if (responseCode == Response.DeviceBusy) {
            camera.onDeviceBusy(this, true);
        }
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, NIKON_START_CAPTURE, 0, 0);
    }
}
