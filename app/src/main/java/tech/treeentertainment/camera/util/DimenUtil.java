
package tech.treeentertainment.camera.util;

import android.content.Context;
import android.util.TypedValue;

public class DimenUtil {

    public static float dpToPx(Context c, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }
}
