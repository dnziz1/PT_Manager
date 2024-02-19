package com.example.pt_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProgramEventWorkoutTypeAdapter extends BaseAdapter {
    public ArrayList<ProgramEventWorkoutTypeModel> workoutTypeList;
    Activity activity;

    public ProgramEventWorkoutTypeAdapter(Activity activity, ArrayList<ProgramEventWorkoutTypeModel> workoutTypeList) {
        super();
        this.activity = activity;
        this.workoutTypeList = workoutTypeList;
    }

    @Override
    public int getCount() {
        return workoutTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return workoutTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mWorkoutTypeID;
        TextView mName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.progevent_workouttype_spinner_row, null);
            holder = new ViewHolder();
            holder.mWorkoutTypeID = (TextView) convertView.findViewById(R.id.rProgEventWorkoutTypeID);
            holder.mName = (TextView) convertView.findViewById(R.id.rProgEventWorkoutTypeName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProgramEventWorkoutTypeModel item = workoutTypeList.get(position);
        holder.mWorkoutTypeID.setText(String.valueOf(item.getWorkoutTypeID()));
        holder.mName.setText(item.getName().toString());

        return convertView;
    }
}



