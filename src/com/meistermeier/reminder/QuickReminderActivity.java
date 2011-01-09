package com.meistermeier.reminder;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QuickReminderActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupMainListView();
    }

    private void setupMainListView() {
        ListView mainListView = (ListView) findViewById(R.id.mainListView);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,new String[]{"Test","Wurst"});
        mainListView.setAdapter(arrayAdapter);

    }
}
