import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationEventAdapter extends RecyclerView.Adapter<NotificationEventAdapter.EventViewHolder> {
    private Context context;
    private List<Activity> activityList;

    public EventAdapter(Context context, List<Activity> activityList) {
        this.context = context;
        this.activityList = activityList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = activityList.get(position);

        holder.activityTitle.setText(event.getTitle());
        holder.activityDetail.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView activityTitle;
        TextView activityDetail;

        public EventViewHolder(View itemView) {
            super(itemView);
            activityTitle = itemView.findViewById(R.id.activityTitle);
            activityDetail = itemView.findViewById(R.id.activityDetail);
        }
    }
}


