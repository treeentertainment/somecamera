package tech.treeentertainment.camera;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import tech.treeentertainment.camera.ptp.FocusPoint;
import tech.treeentertainment.camera.ptp.model.LiveViewData;

public class PictureView extends View {

    private Bitmap picture;

    private float offsetX;
    private float offsetY;
    private float zoom;
    private float minZoom;
    private boolean reset;
    private Matrix matrix;
    private Scroller scroller;

    private LiveViewData data;
    private Paint linePaint;
    private List<FocusPoint> focusPoints;
    private int currentFocusPointId;

    private int oldViewWidth;
    private int oldViewHeight;
    private final float af2[] = new float[2];
    private final float af4[] = new float[4];

    private int viewWidth;
    private int viewHeight;
    private int pictureWidth;
    private int pictureHeight;

    // --- 추가: 마지막 터치 좌표 저장 ---
    private float lastTouchX = -1;
    private float lastTouchY = -1;

    // --- 추가: PictureView 클릭 리스너 인터페이스 ---
    public interface OnPictureClickListener {
        void onPictureClick(float x, float y);
    }

    private OnPictureClickListener pictureClickListener;

    public void setOnPictureClickListener(OnPictureClickListener l) {
        this.pictureClickListener = l;
    }

    public PictureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public PictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PictureView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        matrix = new Matrix();
        scroller = new Scroller(context);
        linePaint = new Paint();
        linePaint.setColor(0xffffffff);
        linePaint.setStrokeWidth(0f);
        focusPoints = new ArrayList<FocusPoint>();
    }

    public float calculatePictureX(float posx) {
        if (pictureWidth != 0) {
            matrix.reset();
            matrix.postTranslate(offsetX, offsetY);
            matrix.postScale(1 / zoom, 1 / zoom);
            af2[0] = posx;
            af2[1] = 0f;
            matrix.mapPoints(af2);
            return af2[0] / pictureWidth;
        } else {
            return posx / getWidth();
        }
    }

    public float calculatePictureY(float posy) {
        if (pictureHeight != 0) {
            matrix.reset();
            matrix.postTranslate(offsetX, offsetY);
            matrix.postScale(1 / zoom, 1 / zoom);
            af2[0] = 0f;
            af2[1] = posy;
            matrix.mapPoints(af2);
            return af2[1] / pictureHeight;
        } else {
            return posy / getHeight();
        }
    }

    public void setPicture(Bitmap picture) {
        if (this.picture != null && picture != null) {
            this.reset = this.picture.getWidth() != picture.getWidth()
                    || this.picture.getHeight() != picture.getHeight();
        } else {
            this.reset = true;
        }
        this.picture = picture;
        this.data = null;
        if (this.picture == null) {
            scroller.abortAnimation();
        }
        invalidate();
    }

    public void setLiveViewData(LiveViewData data) {
        this.reset = this.data == null || data == null;
        this.data = data;
        if (data != null) {
            this.picture = data.getBitmap();
        }
        if (this.picture == null) {
            scroller.abortAnimation();
        }
        invalidate();
    }

    public void setFocusPoints(List<FocusPoint> focusPoints) {
        this.focusPoints = focusPoints;
        invalidate();
    }

    public void setCurrentFocusPoint(int pointId) {
        this.currentFocusPointId = pointId;
        if (!focusPoints.isEmpty()) {
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0x00000000);

        viewWidth = getWidth();
        viewHeight = getHeight();
        pictureWidth = picture != null ? picture.getWidth() : 0;
        pictureHeight = picture != null ? picture.getHeight() : 0;

        if (oldViewHeight != viewHeight || oldViewWidth != viewWidth) {
            reset = true;
            oldViewWidth = viewWidth;
            oldViewHeight = viewHeight;
        }

        if (reset && picture != null) {
            reset = false;
            offsetX = 0;
            offsetY = 0;
            zoom = 1f;
            minZoom = Math.min((float) viewWidth / pictureWidth, (float) viewHeight / pictureHeight);
            zoom = minZoom;
        }

        boolean anim = scroller.computeScrollOffset();
        if (anim) {
            offsetX = scroller.getCurrX();
            offsetY = scroller.getCurrY();
            invalidate();
        }

        if (picture != null) {
            matrix.reset();
            matrix.postScale(zoom, zoom);
            matrix.postTranslate(-offsetX, -offsetY);
            canvas.drawBitmap(picture, matrix, null);
        }

        if (data == null) {
            linePaint.setStrokeWidth(2f);
            int width = picture == null ? viewWidth : pictureWidth;
            int height = picture == null ? viewHeight : pictureHeight;
            for (FocusPoint fp : focusPoints) {
                float left = (fp.posx - fp.radius) * width + 0;
                float top = (fp.posy - fp.radius) * height + 0;
                float len = fp.radius * height * 2; // TODO or width?

                if (fp.id == currentFocusPointId) {
                    linePaint.setColor(0xffff0000);
                } else {
                    linePaint.setColor(0xff000000);
                }

                matrix.reset();
                if (picture != null) {
                    matrix.postScale(zoom, zoom);
                    matrix.postTranslate(-offsetX, -offsetY);
                }

                af2[0] = left;
                af2[1] = top;
                matrix.mapPoints(af2);
                left = af2[0];
                top = af2[1];
                af2[0] = len;
                af2[1] = 0f;
                matrix.mapVectors(af2);
                len = af2[0];

                canvas.drawLine(left, top, left + len, top, linePaint);
                canvas.drawLine(left, top, left, top + len, linePaint);
                canvas.drawLine(left + len, top, left + len, top + len, linePaint);
                canvas.drawLine(left, top + len, left + len, top + len, linePaint);
            }
            linePaint.setStrokeWidth(0f);
        }

        if (data != null) {
            if (data.hasHistogram) {
                int offset = 20;

                linePaint.setColor(0xaa000000);
                canvas.drawRect(offset - 1, viewHeight - 400f, offset + 256f, viewHeight, linePaint);
                data.histogram.position(0);
                for (int i = 0; i < 256; ++i) {
                    int u = Math.min(100, data.histogram.getInt() >> 5);
                    int r = Math.min(100, data.histogram.getInt() >> 5);
                    int g = Math.min(100, data.histogram.getInt() >> 5);
                    int b = Math.min(100, data.histogram.getInt() >> 5);
                    linePaint.setColor(0xffaa0000);
                    canvas.drawLine(offset + i, viewHeight - 0f, offset + i, viewHeight - 0f - r, linePaint);
                    linePaint.setColor(0xff00aa00);
                    canvas.drawLine(offset + i, viewHeight - 100f, offset + i, viewHeight - 100f - g, linePaint);
                    linePaint.setColor(0xff0000aa);
                    canvas.drawLine(offset + i, viewHeight - 200f, offset + i, viewHeight - 200f - b, linePaint);
                    linePaint.setColor(0xffaaaaaa);
                    canvas.drawLine(offset + i, viewHeight - 300f, offset + i, viewHeight - 300f - u, linePaint);
                }
            }

            if (data.hasAfFrame) {
                linePaint.setColor(0xffffffff);

                float left = data.nikonAfFrameCenterX - (data.nikonAfFrameWidth >> 1);
                float top = data.nikonAfFrameCenterY - (data.nikonAfFrameHeight >> 1);
                float right = left + data.nikonAfFrameWidth;
                float bottom = top + data.nikonAfFrameHeight;

                matrix.reset();
                matrix.postScale(zoom, zoom);
                matrix.postTranslate(-offsetX, -offsetY);

                af4[0] = left;
                af4[1] = top;
                af4[2] = right;
                af4[3] = bottom;
                matrix.mapPoints(af4);

                left = af4[0];
                top = af4[1];
                right = af4[2];
                bottom = af4[3];

                canvas.drawLine(left, top, right, top, linePaint);
                canvas.drawLine(left, top, left, bottom, linePaint);
                canvas.drawLine(right, top, right, bottom, linePaint);
                canvas.drawLine(left, bottom, right, bottom, linePaint);
            }
        }
    }

    // --- 추가: performClick() 오버라이드 ---
    @Override
    public boolean performClick() {
        super.performClick();
        if (lastTouchX >= 0 && lastTouchY >= 0 && pictureClickListener != null) {
            float px = calculatePictureX(lastTouchX);
            float py = calculatePictureY(lastTouchY);
            pictureClickListener.onPictureClick(px, py);
        }
        return true;
    }

    // --- 추가: onTouchEvent에서 터치 좌표 저장 ---
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        lastTouchX = event.getX();
        lastTouchY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick(); // 터치 끝나면 클릭 처리
        }
        return false;
    }

    public void zoomAt(float pX, float pY, float distInPixel) {
        if (picture == null) {
            return;
        }
        float dx = -offsetX;
        float dy = -offsetY;

        dx -= pX;
        dy -= pY;

        float s = 1f + distInPixel / 180f; // TODO
        if (zoom * s < minZoom) {
            s = minZoom / zoom;
        } else if (zoom * s > 5f) {
            s = 5f / zoom;
        }

        zoom *= s;

        dx *= s;
        dy *= s;

        dx += pX;
        dy += pY;

        offsetX = -dx;
        offsetY = -dy;

        pan(0, 0);

        invalidate();
    }

    public void pan(float dx, float dy) {
        if (picture == null) {
            return;
        }
        offsetX -= dx;
        offsetY -= dy;

        float toRight = picture.getWidth() * zoom - offsetX;
        if (toRight < getWidth()) {
            offsetX = picture.getWidth() * zoom - getWidth();
        }
        if (offsetX < 0) {
            offsetX = 0;
        }
        float toLeft = picture.getHeight() * zoom - offsetY;
        if (toLeft < getHeight()) {
            offsetY = picture.getHeight() * zoom - getHeight();
        }
        if (offsetY < 0) {
            offsetY = 0;
        }

        invalidate();
    }

    public void fling(float velx, float vely) {
        if (picture == null) {
            return;
        }
        scroller.fling((int) offsetX, (int) offsetY, (int) (-velx * 1f), (int) (-vely * 1f), 0,
                (int) (picture.getWidth() * zoom) - getWidth(), 0, (int) (picture.getHeight() * zoom) - getHeight());
        invalidate();
    }

    public void stopFling() {
        scroller.abortAnimation();
    }
}
