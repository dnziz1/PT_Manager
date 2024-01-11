package com.example.pt_app;

import android.app.Activity;
//import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProgramListLVAdapter extends BaseAdapter {
    public ArrayList<ProgramListLVModel> programList;
    Activity activity;

    public ProgramListLVAdapter(Activity activity, ArrayList<ProgramListLVModel> programList) {
        super();
        this.activity = activity;
        this.programList = programList;
    }

    @Override
    public int getCount() {
        return programList.size();
    }

    @Override
    public Object getItem(int position) {
        return programList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mProgramID;
        TextView mName;
        TextView mDuration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.proglist_listview_row, null);
            holder = new ViewHolder();
            holder.mProgramID = (TextView) convertView.findViewById(R.id.rProgListProgID);
            holder.mName = (TextView) convertView.findViewById(R.id.rProgListName);
            holder.mDuration = (TextView) convertView.findViewById(R.id.rProgListDuration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProgramListLVModel item = programList.get(position);
        holder.mProgramID.setText(String.valueOf(item.getProgID()));
        holder.mName.setText(item.getName().toString());
        holder.mDuration.setText(String.valueOf(item.getDuration()));

        return convertView;
    }
}


