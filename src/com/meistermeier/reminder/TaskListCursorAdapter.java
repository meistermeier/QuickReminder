package com.meistermeier.reminder;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 */
public class TaskListCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;

    public TaskListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.list_layout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        long timestamp = cursor.getLong(2);
        boolean reminder = cursor.getInt(3) == 1;

        TextView taskNameTextView = (TextView) view.findViewById(R.id.taskname_text);
        TextView taskDateTextView = (TextView) view.findViewById(R.id.date_text);
        ImageView statusView = (ImageView) view.findViewById(R.id.status_field);
        ImageView reminderView = (ImageView) view.findViewById(R.id.reminder_view);

        CharSequence formattedDueDate = DateFormat.format("dd.MM.yyyy kk:mm", timestamp);

        taskNameTextView.setText(name);
        taskDateTextView.setText(formattedDueDate);
        if (reminder) {
            reminderView.setImageResource(android.R.drawable.ic_lock_idle_alarm);
        } else {
            reminderView.setImageResource(0);
        }

        if (isDateOverDue(timestamp)) {
            statusView.setBackgroundColor(Color.RED);
        } else {
            statusView.setBackgroundColor(Color.GREEN);
        }
    }

    private boolean isDateOverDue(long timestamp) {
        return timestamp < System.currentTimeMillis();
    }

    @Override
    public Object getItem(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        long timestamp = cursor.getLong(2);
        boolean reminder = cursor.getInt(3) == 1;

        return new DefaultTaskItem(id, name, timestamp, reminder);

    }
}
