package com.example.pt_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassesLVAdapter extends BaseAdapter {
    public ArrayList<ClassesLVModel> classes;
    Activity activity;

    public ClassesLVAdapter(Activity activity, ArrayList<ClassesLVModel> classes) {
        super();
        this.activity = activity;
        this.classes = classes;
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Object getItem(int position) {
        return classes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mClassID;
        TextView mName;
        TextView mDuration;
        TextView mNotes;
        TextView mTrainerIO;
        TextView mTrainerName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassesLVAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.classes_listview_row, null);
            holder = new ClassesLVAdapter.ViewHolder();
            holder.mClassID = (TextView) convertView.findViewById(R.id.rClassesLVClassID);
            holder.mName = (TextView) convertView.findViewById(R.id.rClassesLVName);
            holder.mDuration = (TextView) convertView.findViewById(R.id.rClassesLVDuration);
            holder.mNotes = (TextView) convertView.findViewById(R.id.rClassesLVNotes);
            holder.mTrainerIO = (TextView) convertView.findViewById(R.id.rClassesLVTrainerID);
            holder.mTrainerName = (TextView) convertView.findViewById(R.id.rClassesLVTrainerName);
            convertView.setTag(holder);
        } else {
            holder = (ClassesLVAdapter.ViewHolder) convertView.getTag();
        }

        ClassesLVModel item = classes.get(position);
        holder.mClassID.setText(String.valueOf(item.getClassID()));
        holder.mName.setText(item.getName().toString());
        holder.mDuration.setText(String.valueOf(item.getDuration()));
        holder.mNotes.setText(String.valueOf(item.getNotes()));
        holder.mTrainerIO.setText(String.valueOf(item.getTrainerID()));
        holder.mTrainerName.setText(String.valueOf(item.getTrainerName()));

        return convertView;
    }

}
