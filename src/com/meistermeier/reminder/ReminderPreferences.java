package com.meistermeier.reminder;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 */
public class ReminderPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.reminder_preferences);

    }


}
