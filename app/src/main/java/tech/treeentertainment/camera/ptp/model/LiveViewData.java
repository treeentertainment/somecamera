package tech.treeentertainment.camera.ptp.model;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LiveViewData {
    public byte[] jpegData;

    public Bitmap bitmap;

    public int zoomFactor;
    public int zoomRectLeft;
    public int zoomRectTop;
    public int zoomRectRight;
    public int zoomRectBottom;

    public boolean hasHistogram;
    public ByteBuffer histogram;

    // dimensions are in bitmap size
    public boolean hasAfFrame;
    public int nikonAfFrameCenterX;
    public int nikonAfFrameCenterY;
    public int nikonAfFrameWidth;
    public int nikonAfFrameHeight;

    public int nikonWholeWidth;
    public int nikonWholeHeight;

    /**
     * jpegData → Bitmap 변환
     */
    public Bitmap createBitmap() {
        if (bitmap != null) {
            return bitmap; // 이미 만들어진 경우
        }
        if (jpegData != null) {
            bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length);
        }
        return bitmap;
    }
}
