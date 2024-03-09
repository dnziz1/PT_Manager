package com.example.pt_app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
public class ChatAdapter extends ArrayAdapter<Item> {

    private Context mContext;

    private int mResource;

    public ChatAdapter(Context context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            listItem = inflater.inflate(mResource, parent, false);
        }

        Item currentItem = getItem(position);


        ImageView imageView = listItem.findViewById(R.id.imageView);
        imageView.setImageResource(currentItem.getImageResource());


        TextView titleTextView = listItem.findViewById(R.id.titleTextView);
        titleTextView.setText(currentItem.getTitle());


        TextView descriptionTextView = listItem.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(currentItem.getDescription());

        return listItem;
    }


}
