package com.meistermeier.reminder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Some nice information
 */
public class TaskEditActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);

        // if a extra set -> fill the components with data
        if (getIntent().hasExtra(TaskItem.NAME_FIELD)) {
            fillComponentsWithData();
        }

    }

    private void fillComponentsWithData() {
        Bundle extras = getIntent().getExtras();

        // prepare components from edit view
        EditText nameEditText = (EditText) findViewById(R.id.task_edit_name);
        CheckBox checkBox = (CheckBox) findViewById(R.id.task_edit_reminder);
        DatePicker datePicker = (DatePicker) findViewById(R.id.task_edit_date);
        TimePicker timePicker = (TimePicker) findViewById(R.id.task_edit_time);

        // prepare data
        Date dueDate = (Date) extras.get(TaskItem.DUE_DATE_FIELD);
        Calendar dueCalendar = Calendar.getInstance();
        dueCalendar.setTimeInMillis(dueDate.getTime());

        String taskName = (String) extras.get(TaskItem.NAME_FIELD);
        Boolean reminder = (Boolean) extras.get(TaskItem.REMINDER_FIELD);

        // data for date picker
        int day = dueCalendar.get(Calendar.DAY_OF_MONTH);
        int month = dueCalendar.get(Calendar.MONTH);
        int year = dueCalendar.get(Calendar.YEAR);

        // data for time picker
        int hour = dueCalendar.get(Calendar.HOUR);
        int minute = dueCalendar.get(Calendar.MINUTE);

        // populate the data to the components
        nameEditText.setText(taskName);
        checkBox.setChecked(reminder);
        datePicker.updateDate(year, month, day);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
    }

}