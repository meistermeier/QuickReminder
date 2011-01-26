package com.meistermeier.reminder;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class QuickReminderActivity extends Activity {
    private MenuItem addItem;

    private static final int ADD_MENU_ID = Menu.FIRST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupMainListView();

    }

    @Override
    protected void onResume() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(TaskNotificationReceiver.NOTIFICATION_ID);

        super.onResume();
    }


    private void setupMainListView() {
        ListView mainListView = (ListView) findViewById(R.id.mainListView);

        TaskDBOpenHelper taskDBOpenHelper = new TaskDBOpenHelper(this);
        SQLiteDatabase readableDatabase = taskDBOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query(TaskDBOpenHelper.DB_NAME, null, null, null, null, null, "timestamp");

        mainListView.setAdapter(new TaskListAdapter(this, cursor));

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskItem selectedItem = (TaskItem) adapterView.getItemAtPosition(i);
                editItem(selectedItem);
            }

        });

        readableDatabase.close();
    }

    private void editItem(TaskItem item) {
        Intent editIntent = new Intent(this, TaskEditActivity.class);
        editIntent.putExtra(TaskItem.NAME_FIELD, item.getName());
        editIntent.putExtra(TaskItem.DUE_DATE_FIELD, item.getTimestamp());
        editIntent.putExtra(TaskItem.REMINDER_FIELD, item.isReminderActive());
        editIntent.putExtra(TaskItem.ID_FIELD, item.getId());
        startActivity(editIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ADD_MENU_ID, Menu.NONE, "Add");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case ADD_MENU_ID:
                Intent editIntent = new Intent(this, TaskEditActivity.class);
                startActivity(editIntent);
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }


}
