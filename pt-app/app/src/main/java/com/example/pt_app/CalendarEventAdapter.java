package com.example.pt_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.pt_app.CalendarModel;
import java.util.ArrayList;

public class CalendarEventAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<CalendarModel> eventList;

    public CalendarEventAdapter(Activity activity, ArrayList<CalendarModel> eventList) {
        this.activity = activity;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class EventViewHolder {
        TextView eventId;
        TextView eventTitle;
        TextView eventStartDate;
        TextView eventEndDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.calendar_event_list_item, null);
            holder = new EventViewHolder();
            holder.eventId = convertView.findViewById(R.id.eventId);
            holder.eventTitle = convertView.findViewById(R.id.eventTitle);
            holder.eventStartDate = convertView.findViewById(R.id.eventStartDate);
            holder.eventEndDate = convertView.findViewById(R.id.eventEndDate);
            convertView.setTag(holder);
        } else {
            holder = (EventViewHolder) convertView.getTag();
        }

        CalendarModel event = eventList.get(position);
        holder.eventId.setText(String.valueOf(event.getId()));
        holder.eventTitle.setText(event.getTitle());
        holder.eventStartDate.setText(event.getStartDate());
        holder.eventEndDate.setText(event.getEndDate());

        return convertView;
    }
}
