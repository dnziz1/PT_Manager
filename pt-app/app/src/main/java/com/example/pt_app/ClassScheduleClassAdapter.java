package com.example.pt_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassScheduleClassAdapter extends BaseAdapter {
    public ArrayList<ClassScheduleClassModel> classList;
    Activity activity;

    public ClassScheduleClassAdapter(Activity activity, ArrayList<ClassScheduleClassModel> classList) {
        super();
        this.activity = activity;
        this.classList = classList;
    }

    @Override
    public int getCount() {
        return classList.size();
    }

    @Override
    public Object getItem(int position) {
        return classList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mClassID;
        TextView mName;
        TextView mDuration;
        TextView mMaxOccupancy;
        TextView mNotes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.classsched_class_spinner_row, null);
            holder = new ViewHolder();
            holder.mClassID = (TextView) convertView.findViewById(R.id.rClassSchedClassID);
            holder.mName = (TextView) convertView.findViewById(R.id.rClassSchedClassName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClassScheduleClassModel item = classList.get(position);
        holder.mClassID.setText(String.valueOf(item.getClassID()));
        holder.mName.setText(item.getName().toString());

        return convertView;
    }
}



