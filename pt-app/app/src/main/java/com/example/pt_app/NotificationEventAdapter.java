package com.example.pt_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.media.metrics.Event;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationEventAdapter extends BaseAdapter {
    Activity activity;
    public ArrayList<com.example.pt_app.Notification> activityList;

    public NotificationEventAdapter(Activity activity, ArrayList<com.example.pt_app.Notification> activityList) {
        super();
        this.activity = activity;
        this.activityList = activityList;
    }


    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public Object getItem(int position) {
        return activityList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class EventViewHolder {
        TextView NotifID;
        TextView Title;
        TextView Details;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notification_listview, null);
            holder = new EventViewHolder();
            holder.NotifID = (TextView) convertView.findViewById(R.id.notificationListID);
            holder.Title = (TextView) convertView.findViewById(R.id.notificationListTitle);
            holder.Details = (TextView) convertView.findViewById(R.id.notificationListDetails);
            convertView.setTag(holder);
        } else {
            holder = (EventViewHolder) convertView.getTag();
        }
        com.example.pt_app.Notification item = activityList.get(position);
        holder.NotifID.setText(String.valueOf(item.getNotifID()));
        holder.Title.setText(String.valueOf(item.getTitle()));
        holder.Details.setText(String.valueOf(item.getDetails()));

        return convertView;
    }
}



