package com.example.pt_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProgramEventWorkoutAdapter extends BaseAdapter {
    public ArrayList<ProgramEventWorkoutModel> workoutList;
    Activity activity;

    public ProgramEventWorkoutAdapter(Activity activity, ArrayList<ProgramEventWorkoutModel> workoutList) {
        super();
        this.activity = activity;
        this.workoutList = workoutList;
    }

    @Override
    public int getCount() {
        return workoutList.size();
    }

    @Override
    public Object getItem(int position) {
        return workoutList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mWorkoutID;
        TextView mMuscleGroup;
        TextView mName;
        TextView mLevel;
        TextView mEquipment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.progevent_workouttype_spinner_row, null);
            holder = new ViewHolder();
            holder.mWorkoutID = (TextView) convertView.findViewById(R.id.rProgEventWorkoutID);
            holder.mMuscleGroup = (TextView) convertView.findViewById(R.id.rProgEventWorkoutMuscleGroup);
            holder.mName = (TextView) convertView.findViewById(R.id.rProgEventWorkoutName);
            holder.mLevel = (TextView) convertView.findViewById(R.id.rProgEventWorkoutLevel);
            holder.mEquipment = (TextView) convertView.findViewById(R.id.rProgEventWorkoutEquipment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProgramEventWorkoutModel item = workoutList.get(position);
        holder.mWorkoutID.setText(String.valueOf(item.getWorkoutID()));
        holder.mMuscleGroup.setText(item.getMuscleGroup().toString());
        holder.mName.setText(item.getName().toString());
        holder.mLevel.setText(item.getLevel().toString());
        holder.mEquipment.setText(item.getEquipment().toString());

        return convertView;
    }
}



