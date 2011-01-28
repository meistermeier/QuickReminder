package com.meistermeier.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Some nice information
 */
public class TaskEditActivity extends Activity {

    public static final String TASK_NOTIFICATION_ACTION = "com.meistermeier.reminder.TASK_NOTIFICATION";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);

        // if a extra set -> fill the components with data
        if (getIntent().hasExtra(TaskItem.NAME_FIELD)) {
            fillComponentsWithData();
        }

    }

    public void onSave(View view) {

        EditText nameEditText = (EditText) findViewById(R.id.task_edit_name);
        CheckBox checkBox = (CheckBox) findViewById(R.id.task_edit_reminder);
        DatePicker datePicker = (DatePicker) findViewById(R.id.task_edit_date);
        TimePicker timePicker = (TimePicker) findViewById(R.id.task_edit_time);

        // create calendar
        Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());

        // extract values
        String taskName = nameEditText.getText().toString();
        long timestamp = calendar.getTimeInMillis();
        boolean reminderActive = checkBox.isChecked();

        Bundle extras = getIntent().getExtras();

        long taskId = 0;

        if (extras != null) {
            taskId = extras.getLong(TaskItem.ID_FIELD);
        }


        TaskDBOpenHelper taskDBOpenHelper = new TaskDBOpenHelper(this);
        SQLiteDatabase writableDatabase = taskDBOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskItem.NAME_FIELD, taskName);
        values.put(TaskItem.TIMESTAMP_FIELD, timestamp);
        values.put(TaskItem.REMINDER_FIELD, reminderActive ? 1 : 0);

        // only do a insert if it's a new record (id=0)
        if (taskId == 0) {
            writableDatabase.insert(TaskDBOpenHelper.DB_NAME, null, values);
        } else {
            writableDatabase.update(TaskDBOpenHelper.DB_NAME, values, TaskItem.ID_FIELD + "=?", new String[]{String.valueOf(taskId)});
        }

        writableDatabase.close();

        // put this task (new or just updated) in the AlarmManager
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(TASK_NOTIFICATION_ACTION);

        intent.putExtra("taskname", taskName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 4321, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (reminderActive) {
            alarmManager.set(AlarmManager.RTC, timestamp, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
        }

        this.finish();
    }

    private void fillComponentsWithData() {
        Bundle extras = getIntent().getExtras();

        // prepare components from edit view
        EditText nameEditText = (EditText) findViewById(R.id.task_edit_name);
        CheckBox checkBox = (CheckBox) findViewById(R.id.task_edit_reminder);
        DatePicker datePicker = (DatePicker) findViewById(R.id.task_edit_date);
        TimePicker timePicker = (TimePicker) findViewById(R.id.task_edit_time);

        // prepare data
        long timestamp = (Long) extras.get(TaskItem.TIMESTAMP_FIELD);
        Calendar dueCalendar = Calendar.getInstance();
        dueCalendar.setTimeInMillis(timestamp);

        String taskName = (String) extras.get(TaskItem.NAME_FIELD);
        Boolean reminder = (Boolean) extras.get(TaskItem.REMINDER_FIELD);

        // data for date picker
        int day = dueCalendar.get(Calendar.DAY_OF_MONTH);
        int month = dueCalendar.get(Calendar.MONTH);
        int year = dueCalendar.get(Calendar.YEAR);

        // data for time picker
        int hour = dueCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = dueCalendar.get(Calendar.MINUTE);

        // populate the data to the components
        nameEditText.setText(taskName);
        checkBox.setChecked(reminder);
        datePicker.updateDate(year, month, day);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
    }

}