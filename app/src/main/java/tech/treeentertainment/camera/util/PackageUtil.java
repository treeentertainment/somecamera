
package tech.treeentertainment.camera.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;


public class PackageUtil {

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getApplicationInfo().packageName, 0).versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getApplicationInfo().packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }
}
