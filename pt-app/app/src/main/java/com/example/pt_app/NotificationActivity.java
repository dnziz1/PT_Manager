package com.example.pt_app;

import android.app.Notification;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

public class NotificationActivity extends AppCompactActivity{
    private static final String URL = "http://10.0.2.2:8000/";
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

        notificationList = new ArrayList<>();

        //fetchDetails();
    }

    //new database connection
    ServerConnection serverConnection = new ServerConnection();

//    private void fetchDetails() {
//        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray array) {
//                for (int i=0;i<array.length();i++) {
//                    try {
//                        JSONObject object = array.getJSONObject(i);
//                        String title=object.getString("title").trim();
//                        String description = object.getString("details").trim();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    notificationEventAdapter = new NotificationEventAdapter(NotificationActivity.this,notificationList);
//                    recyclerView.setAdapter(notificationEventAdapter);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(NotificationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        RequestQueue requestQueue = Volley.newRequestQueue(NotificationActivity.this);
//        requestQueue.add((request));
//        }
//    }
}