
package tech.treeentertainment.camera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AspectRatioImageView extends ImageView {

    private int thumbWidth;
    private int thumbHeight;

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0) {
            if (thumbWidth != 0) {
                int height = width * thumbHeight / thumbWidth;
                setMeasuredDimension(width, height);
            } else {
                setMeasuredDimension(width, width);
            }
        } else {
            int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
            setMeasuredDimension(width, height);
        }
    }

    public void setExpectedDimensions(int thumbWidth, int thumbHeight) {
        this.thumbWidth = thumbWidth;
        this.thumbHeight = thumbHeight;
    }
}