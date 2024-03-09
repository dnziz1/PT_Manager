package com.example.pt_app;

import android.app.Notification;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pt_app.NotificationEventAdapter;
import com.mysql.cj.xdevapi.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

public class NotificationActivity extends AppCompatActivity{
    String String = "notifications.php";
    private RecyclerView recyclerView;
    private NotificationEventAdapter notificationEventAdapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notificationEventAdapter);
        notificationList = new ArrayList<>();

        new FetchDetails().execute();
    }

    private class FetchDetails extends AsyncTask<Void, Void, Void> {
        String data = "notifications.php";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(data);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

                reader.close();

                JSONArray jsonArray = new JSONArray(sb.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String details = jsonObject.getString("details");
                    Notification notification = new Notification();
                    notificationList.add(notification);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        //Get result of async process
        public void processFinish(String result, String destination) {

        }
    }
}
