package com.meistermeier.reminder;

import android.app.Notification;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;

/**
 */
public class ReminderPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.reminder_preferences);

    }


}
