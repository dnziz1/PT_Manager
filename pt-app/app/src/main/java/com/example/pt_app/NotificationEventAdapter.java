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

import java.util.List;

public class NotificationEventAdapter extends RecyclerView.Adapter<NotificationEventAdapter.EventViewHolder> {
    Context context;
    List<com.example.pt_app.Notification> activityList;

    public NotificationEventAdapter(Context context, List<com.example.pt_app.Notification> activityList) {
        this.context = context;
        this.activityList = activityList;
    }

    public NotificationEventAdapter(NotificationActivity notificationActivity, List<Notification> notificationList) {
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View notificationLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notification,parent,false);
        return new EventViewHolder(notificationLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationEventAdapter.EventViewHolder holder, int position) {
        com.example.pt_app.Notification notification = activityList.get(position);
        holder.Title.setText(notification.getTitle());
        holder.Details.setText(notification.getDetails());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView Details;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.titleTextView);
            Details=itemView.findViewById(R.id.descriptionTextView);
        }
    }
}



