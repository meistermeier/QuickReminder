package com.meistermeier.reminder;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class QuickReminderActivity extends Activity {

    TaskListCursorAdapter taskListCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        TaskDBOpenHelper taskDBOpenHelper = new TaskDBOpenHelper(this);
        SQLiteDatabase readableDatabase = taskDBOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query(TaskDBOpenHelper.DB_NAME, null, null, null, null, null, "timestamp");

        taskListCursorAdapter = new TaskListCursorAdapter(this, cursor);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.gestureView);
        gestureView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {

                Log.d("QuickReminder", "Some gestures has been performed.");
            }
        });

        setupMainListView();

    }

    @Override
    protected void onResume() {
        checkForAndCancelNotification(getIntent());

        updateTaskList();

        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkForAndCancelNotification(intent);

    }

    private void checkForAndCancelNotification(Intent intent) {
        Log.d("QuickReminder", "New Intent" + intent.getExtras());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("QuickReminder", "NotificationId: " + extras.getLong("tasknotificationid"));
        } else {
            Log.d("QuickReminder", "Extras: NULL");
        }

        if (extras != null && extras.getLong("tasknotificationid") != 0L) {
            notificationManager.cancel((int) extras.getLong("tasknotificationid"));
        } else {
            Log.d("QuickReminder", "TasknotificationId is 0");
        }
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
                        setTitle(R.string.delete_dialog_title).
                        setMessage(R.string.delete_dialog_message+ " " + selectedTask.getName()).
                        setPositiveButton(android.R.string.yes, confirmationListener).
                        setNegativeButton(android.R.string.no, confirmationListener).
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) task.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
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

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu:
                Intent editIntent = new Intent(this, TaskEditActivity.class);
                startActivity(editIntent);
                break;
            case R.id.config_menu:
                Intent intent = new Intent().setClass(this, ReminderPreferences.class);
                startActivity(intent);
                break;
            case R.id.wipe_menu:
                showDeleteAllConfirmDialog();
                break;
            case R.id.gesture_menu:
                Intent gestureIntent = new Intent(this, GestureActivity.class);
                startActivity(gestureIntent);
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void showDeleteAllConfirmDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.wipe_dialog_title)
                .setMessage(R.string.wipe_dialog_message)
                .setPositiveButton(android.R.string.yes, clearDataDialogListener)
                .setNegativeButton(android.R.string.no, clearDataDialogListener)
                .create();

        alertDialog.show();
    }

    final DialogInterface.OnClickListener clearDataDialogListener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialogInterface, int button) {
            switch (button) {
                case AlertDialog.BUTTON_NEGATIVE:
                    dialogInterface.cancel();
                    break;
                case AlertDialog.BUTTON_POSITIVE:
                    TaskDBOpenHelper taskDBOpenHelper = new TaskDBOpenHelper(QuickReminderActivity.this);
                    SQLiteDatabase database = taskDBOpenHelper.getWritableDatabase();
                    database.execSQL("delete from " + TaskDBOpenHelper.DB_NAME);
                    database.close();

                    updateTaskList();
                    dialogInterface.cancel();
                    break;
            }
        }
    };


}
