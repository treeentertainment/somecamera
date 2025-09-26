
package tech.treeentertainment.camera.ptp.model;

import java.nio.ByteBuffer;

import tech.treeentertainment.camera.ptp.PacketUtil;

public class StorageInfo {

    public int storageType;
    public int filesystemType;
    public int accessCapability;
    public long maxCapacity;
    public long freeSpaceInBytes;
    public int freeSpaceInImages;
    public String storageDescription;
    public String volumeLabel;

    public StorageInfo(ByteBuffer b, int length) {
        decode(b, length);
    }

    private void decode(ByteBuffer b, int length) {
        storageType = b.getShort() & 0xffff;
        filesystemType = b.getShort() & 0xffff;
        accessCapability = b.getShort() & 0xff;
        maxCapacity = b.getLong();
        freeSpaceInBytes = b.getLong();
        freeSpaceInImages = b.getInt();
        storageDescription = PacketUtil.readString(b);
        volumeLabel = PacketUtil.readString(b);
    }
}
