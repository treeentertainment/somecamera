package tech.treeentertainment.camera;

import android.app.Application;

import org.acra.ACRA;

// @ReportsCrashes(formKey = "YOUR_KEY_HERE",
// mode = ReportingInteractionMode.NOTIFICATION,
// resNotifTickerText = R.string.crash_notif_ticker_text,
// resNotifTitle = R.string.crash_notif_title,
// resNotifText = R.string.crash_notif_text,
// resNotifIcon = android.R.drawable.stat_notify_error,
// resDialogText = R.string.crash_dialog_text,
// resDialogIcon = android.R.drawable.ic_dialog_info,
// resDialogTitle = R.string.crash_dialog_title,
// resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
// resDialogOkToast = R.string.crash_dialog_ok_toast)
public abstract class AbstractRycApplication extends Application {

    @Override
    public void onCreate() {
        if (AppConfig.USE_ACRA) {
            try {
                ACRA.init(this);
            } catch (Throwable e) {
                // no fail
            }
        }
        super.onCreate();
    }
}
