package com.example.pt_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassesTrainerAdapter extends BaseAdapter {
    public ArrayList<ClassesTrainerModel> trainerList;
    Activity activity;

    public ClassesTrainerAdapter(Activity activity, ArrayList<ClassesTrainerModel> trainerList) {
        super();
        this.activity = activity;
        this.trainerList = trainerList;
    }

    @Override
    public int getCount() {
        return trainerList.size();
    }

    @Override
    public Object getItem(int position) {
        return trainerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mTrainerID;
        TextView mName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.classes_trainer_spinner_row, null);
            holder = new ViewHolder();
            holder.mTrainerID = (TextView) convertView.findViewById(R.id.rClassesTrainerID);
            holder.mName = (TextView) convertView.findViewById(R.id.rClassesTrainerName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ClassesTrainerModel item = trainerList.get(position);
        holder.mTrainerID.setText(String.valueOf(item.getTrainerID()));
        holder.mName.setText(item.getName().toString());

        return convertView;
    }
}



