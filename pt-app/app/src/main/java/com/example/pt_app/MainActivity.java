package com.abs.fitnessapp;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = findViewById(R.id.calendarView);

        // Set a listener to handle date clicks
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the selected date
               // String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
              //  Toast.makeText(MainActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();

                showTimePicker(year, month, dayOfMonth);
            }
        });
    }



    private void showTimePicker(final int year, final int month, final int dayOfMonth) {
        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        handleDateTimeSelection_(year, month, dayOfMonth, hourOfDay, minute);
                    }
                },
                currentHour,
                currentMinute,
                true // 24-hour format
        );

        // Show the time picker dialog
        timePickerDialog.show();
    }

    private void handleDateTimeSelection_(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        // Handle the selected date and time
        String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
        String selectedTime = String.format("%02d:%02d:00", hourOfDay, minute);
        //////////////////////////////////////






        // Parse the selected time to a Date object
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date startDate;
        try {
            startDate = sdf.parse(selectedTime);
        } catch (Exception e) {
            e.printStackTrace();
            return; // Handle parsing error
        }

        // Calculate the end time by adding 1 hour to the start time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        // Format the end time
        String endTime = sdf.format(calendar.getTime());






        //////////////////////////////////////////
        // Create a JSON object with the required parameters
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("sId", 6);
            requestBody.put("trainerId", 116);
            requestBody.put("clientId", 767);
            requestBody.put("eventDate", selectedDate);
            requestBody.put("startTime", selectedTime);
            requestBody.put("endTime", endTime); // Change this according to your requirement
            requestBody.put("title", "Training Session 1");
            requestBody.put("details", "Details of the training session");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send the JSON object to the server using Volley
    //    sendRequestToServer(requestBody);

        new MyAsyncTask().execute(requestBody.toString());


    }

//    private void sendRequestToServer(JSONObject requestBody) {
//        String apiUrl = "https://api.softzonesolution.com/appDb/api.php";
//
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        // Request a string response from the provided URL.
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                apiUrl,
//                requestBody,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Handle the server response
//                        try {
//                            // Process the response if needed
//                            String message = response.getString("message");
//                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle errors
//                        if (error.networkResponse != null && error.networkResponse.data != null) {
//                            try {
//                                String errorResponse = new String(error.networkResponse.data, "UTF-8");
//                                Log.e("Error Response", errorResponse);  // Log the error response
//                                Toast.makeText(MainActivity.this, "Error: " + errorResponse, Toast.LENGTH_SHORT).show();
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Toast.makeText(MainActivity.this, "Error sending request to server", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                );
//
//        // Add the request to the RequestQueue.
//        queue.add(jsonRequest);
//    }


private class MyAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        // Execute the POST request with the JSON payload
        return MyHttpClient.executePostRequest(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the result on the main thread
        handlePostResponse(result);
    }
}

    private void handlePostResponse(String response) {
        // You can update UI or perform any other actions based on the server response
//        Toast.makeText(getApplicationContext(), "Server Response: " + response, Toast.LENGTH_SHORT).show();

        Log.d("response","Server Response: " + response);
    }
}


