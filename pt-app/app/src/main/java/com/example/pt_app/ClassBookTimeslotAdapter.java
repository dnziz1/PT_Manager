package com.example.pt_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassBookTimeslotAdapter extends BaseAdapter {
    public ArrayList<ClassBookTimeslotModel> timeslotList;
    Activity activity;

    public ClassBookTimeslotAdapter(Activity activity, ArrayList<ClassBookTimeslotModel> timeslotList) {
        super();
        this.activity = activity;
        this.timeslotList = timeslotList;
    }

    @Override
    public int getCount() {
        return timeslotList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeslotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mTimeslotID;
        TextView mScheduleID;
        TextView mStartTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.classbook_timeslot_spinner_row, null);
            holder = new ViewHolder();
            holder.mTimeslotID = (TextView) convertView.findViewById(R.id.rClassBookTimeslotID);
            holder.mScheduleID = (TextView) convertView.findViewById(R.id.rClassBookScheduleID);
            holder.mStartTime = (TextView) convertView.findViewById(R.id.rClassBookStartTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClassBookTimeslotModel item = timeslotList.get(position);
        holder.mTimeslotID.setText(String.valueOf(item.getTimeslotID()));
        holder.mScheduleID.setText(String.valueOf(item.getScheduleID()));
        holder.mStartTime.setText(item.getStartTime().toString());

        return convertView;
    }
}



