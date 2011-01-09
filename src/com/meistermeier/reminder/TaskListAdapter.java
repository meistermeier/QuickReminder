package com.meistermeier.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Some nice information
 */
public class TaskListAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;

    public TaskListAdapter(Context context) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return 6;
    }

    public Object getItem(int i) {
        return i;
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
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView1.setText("dummy1");
        viewHolder.textView2.setText("dummy2");

        return view;
    }

    static class ViewHolder {
        TextView textView1;
        TextView textView2;
    }
}
