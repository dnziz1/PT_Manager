package com.example.pt_app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassBookings extends AppCompatActivity implements AsyncResponse {

    int userID, classTimeslotID, classID,classDuration,classTrainerID;
    String className, classTrainerName, classDateTime;

    AsyncResponse asyncResponse;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_bookings);
        asyncResponse = this;
        context = this;

        userID = 99999;

        // SHOW all current classes for the logged in user
        String data = "programs.php?arg1=gcc&arg2=" + userID;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"LISTCLASSES");

    }

    //Get the result of async process
    public void processFinish(String result, String destination){

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
                        // display msg booking deleted successfully
                        new AlertDialog.Builder(context)
                                .setTitle("Cancel Class Booking")
                                .setMessage("The booking was cancelled successfully")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        JSONObject joData;
                        for (int row = 0; row < bookingsFound; row++) {

                            joData = jaData.getJSONObject(row);
                            classTimeslotID = joData.getInt("timeslotID");

                            String dateStr = joData.getString("timestamp");
                            SimpleDateFormat curFormat = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
                            Date dateObj = curFormat.parse(dateStr);
                            SimpleDateFormat newFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm");
                            classDateTime =  newFormat.format(dateObj);
                            classID = joData.getInt("classID");
                            className = joData.getString("timestamp");
                            classDuration = joData.getInt("timestamp");
                            classTrainerID = joData.getInt("timestamp");
                            classTrainerName = joData.getString("timestamp");

                            // create header row
                            //***************************************TO DO




                            // set layout for booked class rows
                            LinearLayout LLC = new LinearLayout(this);
                            LLC.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams paramsC = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            paramsC.setMargins(10, 10, 10, 10);
                            LLC.setLayoutParams(paramsC);
                            final TextView rowTextViewE = new TextView(this);
                            rowTextViewE.setLayoutParams(paramsC);
                            LinearLayout layoutC;

                            // CREATE DYNAMIC CLASS ROWS WITH CANCEL BUTTON
                            // ******* PLACE class and CANCEL button on same line
                            // clicking Cancel button opens a confirmation dialog and then removes the user from the class enrolment table
                            rowTextViewE.setText(classDateTime + " : " + className + " : " + classDuration + " : " + classTrainerName);
                            rowTextViewE.setTag(classTimeslotID);

                            Button btnCancelClass = new Button(this);
                            btnCancelClass.setText("Cancel");
                            // set tags so that relevant data is passed to the database to remove the user from the class
                            btnCancelClass.setTag(classTimeslotID);

                            btnCancelClass.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // OPEN DIALOG BOX TO CONFIRM THE USER WISHES TO CANCEL THE CLASS AND THEN UPDATE DATABASE IF SO

                                    // Ask user to confirm they wish to update the event
                                    new AlertDialog.Builder(context)
                                            .setTitle("Cancel Class Booking?")
                                            .setMessage("Are you sure you wish to cancel this class booking ?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                    // Remove user from the class enrolment table
                                                    String data = "programs.php?arg1=dce&arg2=" + userID + "&arg3=" + classTimeslotID;

                                                    //Create new database connection
                                                    ServerConnection serverConnection = new ServerConnection();
                                                    //Setup response value
                                                    serverConnection.delegate = asyncResponse;
                                                    //Send data to server
                                                    serverConnection.execute(data, "DELETEBOOKING");

                                                }
                                            })

                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    // Do nothing
                                                    dialog.dismiss();
                                                }
                                            })

                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            });

                            // add day and Add event button on the same row
                            LLC.addView(rowTextViewE);
                            LLC.addView(btnCancelClass);
                            // add the textview to the linearlayout
                            layoutC = (LinearLayout) findViewById(R.id.classBookingsLayout);
                            layoutC.addView(LLC);
                        }
                    }
                }
            }
        } catch (Exception e) {
            new AlertDialog.Builder(context)
                    .setTitle("Class Bookings - Serious Error")
                    .setMessage(e.getMessage())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the Classes activity
        finish();
    }

}
