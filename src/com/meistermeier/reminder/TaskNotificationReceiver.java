package com.meistermeier.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 */
public class TaskNotificationReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "TestEvent", Toast.LENGTH_LONG).show();

        Notification notification = new Notification(0, "Tickertext", System.currentTimeMillis());
        notification.contentIntent= PendingIntent.getActivity(context,0, intent, 0);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify("tagname", 1234, notification);

    }

}
