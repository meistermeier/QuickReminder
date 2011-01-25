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

        int icon = android.R.drawable.alert_dark_frame;
        Notification notification = new Notification(icon, "things to do", System.currentTimeMillis());

        CharSequence contentTitle = "QuickReminder";
        CharSequence contentText = intent.getExtras().getString("taskname");
        Intent notificationIntent = new Intent(context, QuickReminderActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1234, notification);

    }

}
