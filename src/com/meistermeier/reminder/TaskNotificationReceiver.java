package com.meistermeier.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

/**
 */
public class TaskNotificationReceiver extends BroadcastReceiver {

    public final static int NOTIFICATION_ID = 21412;

    public void onReceive(Context context, Intent intent) {

        int icon = android.R.drawable.alert_dark_frame;
        Notification notification = new Notification(icon, "things to do", System.currentTimeMillis());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean ringtoneActive = preferences.getBoolean("ringtone_active", false);
        String ringtone = preferences.getString("ringtone_selected", Settings.System.DEFAULT_RINGTONE_URI.toString());

        boolean ledActive = preferences.getBoolean("led_active", false);

        boolean vibrateActive = preferences.getBoolean("vibrate_active", false);

        Log.d("QuickReminder", "Ringtone is " + (ringtoneActive ? "active" : "not active"));

        if (ringtoneActive) {
            Log.d("QuickReminder", "Selected Ringtone: " + ringtone);
            notification.sound = Uri.parse(ringtone);
        }

        if (ledActive) {
            notification.ledOnMS = 1000;
            notification.ledOffMS = 1000;
            notification.ledARGB = Color.argb(0, 0, 100, 100);
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        }

        if (vibrateActive) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }

        CharSequence contentTitle = "QuickReminder";

        CharSequence contentText = intent.getExtras().getString("taskname");
        long taskNotificationId = intent.getExtras().getLong("taskid");

        Log.d("QuickReminder", "Creating notification with id " + (int) taskNotificationId);

        Intent notificationIntent = new Intent(context, QuickReminderActivity.class);
        notificationIntent.putExtra("tasknotificationid", taskNotificationId);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.setData(Uri.parse("quickReminder://" + taskNotificationId));

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) taskNotificationId, notification);

    }

}
