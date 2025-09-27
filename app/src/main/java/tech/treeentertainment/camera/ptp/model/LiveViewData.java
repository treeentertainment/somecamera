package tech.treeentertainment.camera.ptp.model;

import java.nio.ByteBuffer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
public class LiveViewData {
    // 카메라에서 받은 JPEG 이미지 데이터
    private byte[] jpegData;
    // 변환된 Bitmap 객체
    private Bitmap bitmap;
    // 비동기 프레임 준비 콜백
    private FrameReadyListener listener;

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
     * 프레임 준비 콜백 인터페이스
     */
    public interface FrameReadyListener {
        void onFrameReady(Bitmap bmp);
    }

    // 프레임 준비 리스너 등록
    public void setFrameReadyListener(FrameReadyListener listener) {
        this.listener = listener;
        // 이미 데이터가 있으면 바로 콜백 호출 (옵션)
        if (jpegData != null) {
            listener.onFrameReady(createBitmap());
        }
    }

    // 카메라에서 JPEG byte[] 데이터가 도착했을 때 호출
    public void setFrameData(byte[] data) {
        this.jpegData = data;
        Bitmap bmp = createBitmap();
        if (listener != null) {
            listener.onFrameReady(bmp);
        }
    }

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

    public Bitmap getBitmap() {
        return bitmap;
    }
    // LiveViewData.java
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (listener != null) {
            // 반드시 메인스레드에서 실행!
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> listener.onFrameReady(bitmap));
        }
    }
}