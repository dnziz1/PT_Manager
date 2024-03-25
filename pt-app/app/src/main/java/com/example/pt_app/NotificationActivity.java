package com.example.pt_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements AsyncResponse {

    AsyncResponse asyncResponse;
    Button btnRefresh;
    ListView notifs;
    NotificationEventAdapter notificationEventAdapter;
    ArrayList<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_main);
        asyncResponse = this;

        notificationList = new ArrayList<>();
        notifs = findViewById(R.id.notifListLV);
        notificationEventAdapter = new NotificationEventAdapter(this, notificationList);
        notifs.setAdapter(notificationEventAdapter);

        notifs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification clickedNotification = notificationList.get(position);
                Toast.makeText(NotificationActivity.this,
                        "Title : " + clickedNotification.getTitle() + "\n"
                                + "Details : " + clickedNotification.getDetails() + "\n",
                        Toast.LENGTH_SHORT).show();
            }
        });

        notifs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Initialise heading for notification page
                TextView notificationTextView = findViewById(R.id.notificationTextView);
                // Get the clicked notification
                com.example.pt_app.Notification clickedNotification = notificationList.get(position);

                // Remove the clicked notification from the list
                notificationList.remove(position);

                // Notify the adapter that the dataset has changed
                notificationEventAdapter.notifyDataSetChanged();

                // Display a toast or perform any other action indicating that the notification has been viewed
                Toast.makeText(getApplicationContext(), "Notification viewed: " + clickedNotification.getTitle(), Toast.LENGTH_SHORT).show();

                // Check if the notificationlist is empty
                if (notificationList.isEmpty()) {
                    // Update the notificationTextView to display "No Notifications"
                    notificationTextView.setText("No Notifications");
                }
            }
        });

        btnRefresh = findViewById(R.id.notifListRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateListViewData();
            }
        });

        // Toolbar buttons for navigation
        ImageView home = findViewById(R.id.home);
        ImageView notification = findViewById(R.id.notification);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // No need to navigate to the same activity again
                Toast.makeText(NotificationActivity.this, "Already on Notification Page", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch notification data from the server
        String data = "notifications.php";
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.delegate = asyncResponse;
        serverConnection.execute(data, "listNotifications");
    }

    @Override
    public void processFinish(String result, String destination) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.optString("status");

            if (status.equals("OK")) {
                JSONArray jsonData = jsonObject.optJSONArray("data");

                // Check if new data is available
                if (jsonData != null && jsonData.length() > 0) {
                    // New data available, update list view
                    PopulateNotificationListView(jsonData);
                } else {
                    // No new data available, show a message or toast indicating so
                    Toast.makeText(this, "No new notifications available", Toast.LENGTH_SHORT).show();
                }
            } else {
                String errorMessage = jsonObject.optString("msg");
                Log.d("Error", errorMessage);
                new AlertDialog.Builder(this)
                        .setTitle("Error Retrieving Notifications")
                        .setMessage(errorMessage)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", null)
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void PopulateNotificationListView(JSONArray ja) {
        notificationList.clear();

        if (ja != null && ja.length() > 0) {
            // Notifications available, change text view
            TextView notificationTextView = findViewById(R.id.notificationTextView);
            notificationTextView.setText("New Notifications");

            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    Notification notification = new Notification(
                            jo.optInt("sId"),
                            jo.optString("title"),
                            jo.optString("details")
                    );
                    notificationList.add(notification);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            // No notifications available, set default text
            TextView notificationTextView = findViewById(R.id.notificationTextView);
            notificationTextView.setText("No Notifications");
        }

        notificationEventAdapter.notifyDataSetChanged();
    }

    private void UpdateListViewData() {
        // Fetch notification data from the server
        String data = "notifications.php";
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.delegate = asyncResponse;

        // Execute the server connection asynchronously
        serverConnection.execute(data, "listNotifications");
    }

}
