package com.meistermeier.reminder;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class QuickReminderActivity extends Activity {

    private static final int ADD_MENU_ID = 1;
    private static final int WIPE_MENU_ID = 2;
    TaskListCursorAdapter taskListCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TaskDBOpenHelper taskDBOpenHelper = new TaskDBOpenHelper(this);
        SQLiteDatabase readableDatabase = taskDBOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query(TaskDBOpenHelper.DB_NAME, null, null, null, null, null, "timestamp");

        taskListCursorAdapter = new TaskListCursorAdapter(this, cursor);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupMainListView();

    }

    @Override
    protected void onResume() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.cancel(TaskNotificationReceiver.NOTIFICATION_ID);
        // just temp
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getLong("tasknotificationid")!=0L) {
            notificationManager.cancel((int)extras.getLong("tasknotificationid"));
        }

        updateTaskList();

        super.onResume();
    }


    private void setupMainListView() {
        ListView mainListView = (ListView) findViewById(R.id.mainListView);

        mainListView.setAdapter(taskListCursorAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                TaskItem selectedTask = (TaskItem) taskListCursorAdapter.getItem(i);

                editItem(selectedTask);
            }

        });

        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TaskItem selectedTask = (TaskItem) taskListCursorAdapter.getItem(i);

                DialogInterface.OnClickListener confirmationListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int button) {
                        switch (button) {
                            case Dialog.BUTTON_POSITIVE:
                                Log.d("QuickReminder", "Deleting task " + selectedTask);

                                deleteTask(selectedTask);

                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                dialogInterface.cancel();
                                break;
                            default:
                                dialogInterface.cancel();
                        }
                    }
                };

                AlertDialog alertDialog = new AlertDialog.Builder(QuickReminderActivity.this).
                        setTitle("Delete Task").
                        setMessage(selectedTask.getName()).
                        setPositiveButton("Yes", confirmationListener).
                        setNegativeButton("No", confirmationListener).
                        create();

                alertDialog.show();

                return true;
            }
        });

    }

    private void editItem(TaskItem item) {
        Intent editIntent = new Intent(this, TaskEditActivity.class);
        editIntent.putExtra(TaskItem.NAME_FIELD, item.getName());
        editIntent.putExtra(TaskItem.TIMESTAMP_FIELD, item.getTimestamp());
        editIntent.putExtra(TaskItem.REMINDER_FIELD, item.isReminderActive());
        editIntent.putExtra(TaskItem.ID_FIELD, item.getId());
        startActivity(editIntent);
    }

    public void updateTaskList() {
        taskListCursorAdapter.getCursor().requery();
    }

    private void deleteTask(TaskItem task) {
        Intent intent = new Intent(TaskEditActivity.TASK_NOTIFICATION_ACTION);

        intent.putExtra("taskname", task.getName());
        intent.putExtra("taskid", task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)task.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        TaskDBOpenHelper taskDBOpenHelper = new TaskDBOpenHelper(this);
        SQLiteDatabase database = taskDBOpenHelper.getWritableDatabase();
        database.delete(TaskDBOpenHelper.DB_NAME, TaskItem.ID_FIELD + "=?", new String[]{String.valueOf(task.getId())});
        database.close();
        updateTaskList();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ADD_MENU_ID, Menu.NONE, "Add");
        menu.add(0, WIPE_MENU_ID, Menu.NONE, "Wipe Data");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case ADD_MENU_ID:
                Intent editIntent = new Intent(this, TaskEditActivity.class);
                startActivity(editIntent);
                break;
            case WIPE_MENU_ID:
                TaskDBOpenHelper taskDBOpenHelper = new TaskDBOpenHelper(this);
                SQLiteDatabase database = taskDBOpenHelper.getWritableDatabase();
                database.execSQL("delete from " + TaskDBOpenHelper.DB_NAME);
                database.close();

                updateTaskList();
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }


}
