package com.meistermeier.reminder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Some nice information
 */
public class TaskEditActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            fillWithExtraData();
        }

    }

    private void fillWithExtraData() {
        Bundle extras = getIntent().getExtras();

        EditText nameEditText = (EditText) findViewById(R.id.task_edit_name);
        nameEditText.setText((String)extras.get(TaskItem.NAME_FIELD));
    }

}