package tech.treeentertainment.camera;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;

import android.graphics.Color;
import android.content.res.ColorStateList;

public class PropertyToggle extends MaterialButton {

    public PropertyToggle(Context context) {
        super(context);
        init();
    }

    public PropertyToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PropertyToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 토글 버튼처럼 동작
        setCheckable(true);
        setChecked(false);

        // 스타일 기본값
        setCornerRadius(0);
        setStrokeWidth(2);
        setElevation(0);

        // 상태별 색 지정
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},   // 체크됨
                new int[]{-android.R.attr.state_checked}   // 체크 안됨
        };

        int[] colors = new int[]{
                Color.parseColor("#666666"), // 체크됨 배경
                Color.parseColor("#FFFFFFFF")  // 체크 안됨 배경
        };

        setBackgroundTintList(new ColorStateList(states, colors));

        int[] strokeColors = new int[]{
                Color.parseColor("#666666"), // 체크됨 테두리
                Color.parseColor("#FF888888")  // 체크 안됨 테두리
        };

        setStrokeColor(new ColorStateList(states, strokeColors));
    }

}
