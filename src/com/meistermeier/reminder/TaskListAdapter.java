package com.meistermeier.reminder;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Some nice information
 */
public class TaskListAdapter extends BaseAdapter {

    private final List<TaskItem> taskItemList;

    private final LayoutInflater layoutInflater;

    public TaskListAdapter(Context context, List<TaskItem> taskItemList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.taskItemList = taskItemList;

    }

    public int getCount() {
        return taskItemList.size();
    }

    public Object getItem(int i) {
        return taskItemList.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();

            view = layoutInflater.inflate(R.layout.list_layout, null);

            viewHolder.textView1 = (TextView) view.findViewById(R.id.list_text1);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.list_text2);
            viewHolder.statusView = (ImageView) view.findViewById(R.id.status_field);
            viewHolder.reminderView = (ImageView) view.findViewById(R.id.reminder_view);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();

        }

        TaskItem item = (TaskItem) getItem(i);

        Date dueDate = item.getDueDate();
        CharSequence formattedDueDate = DateFormat.format("dd.MM.yyyy hh:mm", dueDate);

        String itemName = item.getName();

        boolean itemReminderActive = item.isReminderActive();

        viewHolder.textView1.setText(itemName);

        viewHolder.textView2.setText(formattedDueDate);

        if (itemReminderActive) {
            Log.d("QuickReminder", "Reminder active for " + itemName);
            viewHolder.reminderView.setImageResource(android.R.drawable.ic_lock_idle_alarm);
        }
        else {
            viewHolder.reminderView.setImageResource(0);
        }

        if (isDateOverDue(dueDate)) {

            viewHolder.statusView.setBackgroundColor(Color.RED);
        }
        else {
            viewHolder.statusView.setBackgroundColor(Color.GREEN);
        }


        return view;
    }

    private boolean isDateOverDue(Date dueDate) {
        Date now = new Date();

        if (dueDate.before(now)) {
            return true;

        }

        return false;
    }

    static class ViewHolder {
        TextView textView1;
        TextView textView2;
        ImageView statusView;
        ImageView reminderView;
    }
}