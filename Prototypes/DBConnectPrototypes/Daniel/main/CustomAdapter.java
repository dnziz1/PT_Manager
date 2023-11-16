package com.dnk.databaseexample1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList _id, event_name, event_trainer, event_time;
    CustomAdapter(Context context,
                  ArrayList _id,
                  ArrayList event_name,
                  ArrayList event_trainer,
                  ArrayList event_time) {
        this.context = context;
        this._id = _id;
        this.event_name = event_name;
        this.event_trainer = event_trainer;
        this.event_time = event_time;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder();
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder._id_txt.setText(String.valueOf(_id.get(position)));
        holder.event_name_txt.setText(String.valueOf(event_name.get(position)));
        holder.event_trainer_txt.setText(String.valueOf(event_trainer.get(position)));
        holder.event_time_txt.setText(String.valueOf(event_time.get(position)));
    }

    @Override
    public int getItemCount() {
        return _id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _id_txt, event_name_txt, event_trainer_txt, event_time_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            _id_txt = itemView.findViewById(R.id._id_txt);
            event_name_txt = itemView.findViewById(R.id.event_name_txt);
            event_trainer_txt = itemView.findViewById(R.id.event_trainer_txt);
            event_time_txt = itemView.findViewById(R.id.event_time_txt);
        }
    }
}
