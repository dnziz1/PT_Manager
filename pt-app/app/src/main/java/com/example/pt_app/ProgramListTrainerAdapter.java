package com.example.pt_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProgramListTrainerAdapter extends BaseAdapter {
    public ArrayList<ProgramListTrainerModel> trainerList;
    Activity activity;

    public ProgramListTrainerAdapter(Activity activity, ArrayList<ProgramListTrainerModel> trainerList) {
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
            convertView = inflater.inflate(R.layout.proglist_listview_row, null);
            holder = new ViewHolder();
            holder.mTrainerID = (TextView) convertView.findViewById(R.id.rProgListTrainerID);
            holder.mName = (TextView) convertView.findViewById(R.id.rProgListTrainerName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProgramListTrainerModel item = trainerList.get(position);
        holder.mTrainerID.setText(String.valueOf(item.getTrainerID()));
        holder.mName.setText(item.getName().toString());

        return convertView;
    }
}



