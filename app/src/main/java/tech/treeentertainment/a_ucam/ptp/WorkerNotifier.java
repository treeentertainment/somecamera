package tech.treeentertainment.a_ucam.ptp;

import android.app.NotificationManager;
import android.content.Context;
import android.app.Notification;
import androidx.core.app.NotificationCompat;

import tech.treeentertainment.a_ucam.R;
import tech.treeentertainment.a_ucam.util.NotificationIds;

public class WorkerNotifier implements Camera.WorkerListener {

    private final NotificationManager notificationManager;
    private final Notification notification;
    private final int uniqueId;
    private static final String CHANNEL_ID = "worker_channel";

    public WorkerNotifier(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        // You should create the notification channel elsewhere if targeting API 26+
        notification = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(context.getString(R.string.worker_content_title))
                .setContentText(context.getString(R.string.worker_content_text))
                .setTicker(context.getString(R.string.worker_ticker))
                .setAutoCancel(false)
                .build();
        uniqueId = NotificationIds.getInstance().getUniqueIdentifier(WorkerNotifier.class.getName() + ":running");
    }

    @Override
    public void onWorkerStarted() {
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(uniqueId, notification);
    }

    @Override
    public void onWorkerEnded() {
        notificationManager.cancel(uniqueId);
    }

}
