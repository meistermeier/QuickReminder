package com.meistermeier.reminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuickReminderActivity extends Activity {
    private MenuItem addItem;

    private static final int ADD_MENU_ID = Menu.FIRST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupMainListView();

    }

    private void setupMainListView() {
        ListView mainListView = (ListView) findViewById(R.id.mainListView);

        // Setup sample data
        TaskItem item1 = new DefaultTaskItem();
        item1.setName("Task 1");
        item1.setDueDate(new Date());
        item1.setReminderActive(false);

        TaskItem item2 = new DefaultTaskItem();
        item2.setName("Ziemlich langer Taskname");
        item2.setDueDate(new Date());
        item2.setReminderActive(true);

        TaskItem item3 = new DefaultTaskItem();
        item3.setName("Ziemlich langer Taskname noch laenger");
        item3.setDueDate(new Date());
        item3.setReminderActive(true);

        List<TaskItem> taskItemList = new ArrayList<TaskItem>();
        taskItemList.add(item1);
        taskItemList.add(item2);
        taskItemList.add(item3);

        mainListView.setAdapter(new TaskListAdapter(this, taskItemList));

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskItem selectedItem = (TaskItem) adapterView.getItemAtPosition(i);
                editItem(selectedItem);
            }

        });

    }

    private void editItem(TaskItem item) {
        Intent editIntent = new Intent(this, TaskEditActivity.class);
        editIntent.putExtra(TaskItem.NAME_FIELD, item.getName());
        editIntent.putExtra(TaskItem.DUE_DATE_FIELD, item.getDueDate());
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
