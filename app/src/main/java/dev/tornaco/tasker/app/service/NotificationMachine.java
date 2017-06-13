package dev.tornaco.tasker.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.v4.app.NotificationCompat;

import dev.tornaco.tasker.app.R;

/**
 * Created by Nick on 2017/6/12 15:04
 */

class NotificationMachine {

    static void buildNotification(Context context, String title, String summary, int id) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_noti_icon_module_installed)
                        .setContentTitle(title)
                        .setContentText(summary);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }

    static void buildFullScreenNotification(Context context, String title, String summary, int id) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_noti_icon_module_installed)
                        .setContentTitle(title)
                        .setContentText(summary)
                        .setFullScreenIntent(PendingIntent.getActivity(InstrumentationRegistry.getTargetContext()
                                , 0x9, new Intent("android.action.TEST"), PendingIntent.FLAG_CANCEL_CURRENT), true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }
}
