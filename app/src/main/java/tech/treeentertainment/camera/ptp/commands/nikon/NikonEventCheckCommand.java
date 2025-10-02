
package tech.treeentertainment.camera.ptp.commands.nikon;

import java.nio.ByteBuffer;

import android.util.Log;

import tech.treeentertainment.camera.AppConfig;
import tech.treeentertainment.camera.ptp.NikonCamera;
import tech.treeentertainment.camera.ptp.PtpCamera.IO;
import tech.treeentertainment.camera.ptp.PtpConstants;
import tech.treeentertainment.camera.ptp.PtpConstants.Event;
import tech.treeentertainment.camera.ptp.PtpConstants.Operation;

public class NikonEventCheckCommand extends NikonCommand {

    private static final String TAG = NikonEventCheckCommand.class.getSimpleName();

    public NikonEventCheckCommand(NikonCamera camera) {
        super(camera);
    }

    @Override
    public void exec(IO io) {
        io.handleCommand(this);
    }

    @Override
    public void encodeCommand(ByteBuffer b) {
        encodeCommand(b, Operation.NikonGetEvent);
    }

    @Override
    protected void decodeData(ByteBuffer b, int length) {
        int count = b.getShort();

        while (count > 0) {
            --count;

            int eventCode = b.getShort();
            int eventParam = b.getInt();

            if (AppConfig.LOG) {
                Log.i(TAG,
                        String.format("event %s value %s(%04x)", PtpConstants.eventToString(eventCode),
                                PtpConstants.propertyToString(eventParam), eventParam));
            }

            switch (eventCode) {
            case Event.ObjectAdded:
                camera.onEventObjectAdded(eventParam);
                break;
            case Event.DevicePropChanged:
                camera.onEventDevicePropChanged(eventParam);
                break;
            case Event.CaptureComplete:
                camera.onEventCaptureComplete();
                break;
            }
        }
    }
}
