package com.example.pt_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity implements AsyncResponse {

    CalendarView calendarView;
    ListView eventListView;
    Calendar calendar;
    ArrayList<CalendarModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.eventListView);
        calendar = Calendar.getInstance();
        eventList = new ArrayList<>();

        // Set up listener for date change events
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Get the selected date
                calendar.set(year, month, dayOfMonth);

                // Fetch events for the selected date
                fetchEventsForDate(calendar.getTime());
            }
        });

        // Fetch event data from the server
        String data = "calendar.php";
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.delegate = this;
        serverConnection.execute(data, "listCalendar");

        UpdateListViewData();

        ImageView home = findViewById(R.id.home);
        ImageView notification = findViewById(R.id.notification);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchEventsForDate(Date selectedDate) {
        // Filter events for the selected date
        ArrayList<CalendarModel> eventsForDate = new ArrayList<>();
        for (CalendarModel event : eventList) {
            if (isSameDate(event.getStartDate(), selectedDate)) {
                eventsForDate.add(event);
            }
        }
        // Update ListView with events for the selected date
        updateEventListView(eventsForDate);
    }

    private boolean isSameDate(String date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String strDate2 = sdf.format(date2);
        return date1.equals(strDate2);
    }

    private void updateEventListView(ArrayList<CalendarModel> events) {
        if (!events.isEmpty()) {
            // Display events for the selected date in ListView
            CalendarEventAdapter adapter = new CalendarEventAdapter(CalendarActivity.this, events);
            eventListView.setAdapter(adapter);
        } else {
            // No events for the selected date
            eventListView.setAdapter(null);
            Toast.makeText(CalendarActivity.this, "No events for selected date", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String result, String destination) {
        if (destination.equals("listCalendar")) {
            // Handle server response for calendar data
            try {
                JSONArray jsonArray = new JSONArray(result);
                eventList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String startDate = jsonObject.getString("start_date");
                    String endDate = jsonObject.getString("end_date");
                    eventList.add(new CalendarModel(id, title, startDate, endDate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // AsyncTask to fetch events from the server
    private void UpdateListViewData() {

        // Fetch events from the server here
        String data = "calendar.php?date="; // Assuming you have a method to get the current date
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.delegate = CalendarActivity.this; // Assuming CalendarActivity implements AsyncResponse
        serverConnection.execute(data, "listCalendar");

    }
}

