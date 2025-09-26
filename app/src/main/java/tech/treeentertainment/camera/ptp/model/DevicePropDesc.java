
package tech.treeentertainment.camera.ptp.model;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PacketUtil;
import tech.treeentertainment.camera.ptp.PtpConstants.Datatype;

public class DevicePropDesc {

    public int code;
    public int datatype;
    public boolean readOnly;
    public int factoryDefault;
    public int currentValue;
    public int[] description;

    public DevicePropDesc() {
    }

    public DevicePropDesc(ByteBuffer b, int length) {
        decode(b, length);
    }

    public void decode(ByteBuffer b, int length) {
        code = b.getShort() & 0xFFFF;
        datatype = b.getShort() & 0xFFFF;
        readOnly = b.get() == 0;

        if (datatype == Datatype.int8 || datatype == Datatype.uint8) {
            factoryDefault = b.get() & 0xFF;
            currentValue = b.get() & 0xFF;
            int form = b.get();
            if (form == 2) {
                description = PacketUtil.readU8Enumeration(b);
            } else if (form == 1) {
                int mini = b.get();
                int maxi = b.get();
                int step = b.get();
                description = new int[(maxi - mini) / step + 1];
                for (int i = 0; i < description.length; ++i) {
                    description[i] = mini + step * i;
                }
            }
        } else if (datatype == Datatype.uint16) {
            factoryDefault = b.getShort() & 0xFFFF;
            currentValue = b.getShort() & 0xFFFF;
            int form = b.get();
            if (form == 2) {
                description = PacketUtil.readU16Enumeration(b);
            } else if (form == 1) {
                int mini = b.getShort() & 0xFFFF;
                int maxi = b.getShort() & 0xFFFF;
                int step = b.getShort() & 0xFFFF;
                description = new int[(maxi - mini) / step + 1];
                for (int i = 0; i < description.length; ++i) {
                    description[i] = mini + step * i;
                }
            }
        } else if (datatype == Datatype.int16) {
            factoryDefault = b.getShort();
            currentValue = b.getShort();
            int form = b.get();
            if (form == 2) {
                description = PacketUtil.readS16Enumeration(b);
            } else if (form == 1) {
                int mini = b.getShort();
                int maxi = b.getShort();
                int step = b.getShort();
                description = new int[(maxi - mini) / step + 1];
                for (int i = 0; i < description.length; ++i) {
                    description[i] = mini + step * i;
                }
            }
        } else if (datatype == Datatype.int32 || datatype == Datatype.uint32) {
            factoryDefault = b.getInt();
            currentValue = b.getInt();
            int form = b.get();
            if (form == 2) {
                description = PacketUtil.readU32Enumeration(b);
            }
        }

        if (description == null) {
            description = new int[0];
        }
    }
}
