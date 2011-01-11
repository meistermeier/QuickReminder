package com.meistermeier.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: Gerrit
 * Date: 11.01.11
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
public class TaskNotificationReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "TestEvent", Toast.LENGTH_LONG).show();
    }
}
