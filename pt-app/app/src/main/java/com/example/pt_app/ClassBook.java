package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClassBook extends AppCompatActivity implements AsyncResponse {
    AsyncResponse asyncResponse;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_book);
        asyncResponse = this;
        context = this;
    }

    //Get the result of async process
    public void processFinish(String result, String destination) {

        try {
            // CONVERT RESULT STRING TO JSON OBJECT
            JSONObject jo = null;
            jo = new JSONObject(result);

            if (jo.length() > 0) {
                if (jo.getString("status").equals("Error")) {
                    Log.d("Error", jo.getString("msg"));

                    new AlertDialog.Builder(context)
                            .setTitle("Error Retrieving Class Bookings")
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;
                    int bookingsFound = 0;

                    if (jo.getString("status").equals("OK")) {
                        jaData = jo.getJSONArray("data");
                        bookingsFound = jaData.length();
                    }

                    if (destination.equals("DELETEBOOKING")) {

                    }
                }
            }
        } catch(Exception e){
            new AlertDialog.Builder(context)
                    .setTitle("Class Book - Serious Error")
                    .setMessage(e.getMessage())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
                @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the Class info activity
        finish();
    }

}