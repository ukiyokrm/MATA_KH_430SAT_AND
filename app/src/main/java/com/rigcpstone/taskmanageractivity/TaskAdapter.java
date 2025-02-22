package com.rigcpstone.taskmanageractivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;

    public TaskAdapter(Context context, List<String> tasks) {
        super(context, R.layout.task_item, tasks);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_item, parent, false);
            holder = new ViewHolder();
            holder.taskText = convertView.findViewById(R.id.taskText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.taskText.setText(getItem(position));
        return convertView;
    }

    static class ViewHolder {
        TextView taskText;
    }
}