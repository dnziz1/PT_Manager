package com.example.pt_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    int userID, classBookingID, classID,classTrainerID;
    String className, classTrainerName, classDateTime,accountType;

    AsyncResponse asyncResponse;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_bookings);
        asyncResponse = this;
        context = this;

        // Get userid and account type and set screen accordingly
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID",0);
        accountType = intent.getStringExtra("accountType");

//        // TEST
//        userID = 99999;

        // SHOW all current classes for the logged in user
        String data = "classes.php?arg1=gcbbc&arg2=" + userID;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"LISTCLASSES");

    }

    //Get the result of async process
    public void processFinish(String result, String destination) {

        // First check there wasn't a problem getting session data
/*        if (result.contains("Session unavailable")) {
            // display error message and on clicking ok finish all activities and load the login activity

            new AlertDialog.Builder(context)
                    .setTitle("Error Retrieving Session Data")
                    .setMessage(result)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close all activities and relaunch the login activity
                            Intent intent = new Intent(context, ClientLogin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })

                    .show();
        } else {
*/
        // CONVERT RESULT STRING TO JSON ARRAY
        JSONObject jo = null;
        try {
            // Get the data which is on line 3 after the session data message
            String lines[] = result.split("\\r?\\n");
            //jo = new JSONObject(result);
            jo = new JSONObject(lines[2]);

            String errTitle = "";

            if (destination.equals("LISTCLASSES")) {
                errTitle = "Error Retrieving Class Bookings";
            } else if (destination.equals("DELETEBOOKING")) {
                errTitle = "Error Deleting Booking";
            }

            if (jo.length() > 0) {
                if (jo.getString("status").equals("Error")) {
                    Log.d("Error", jo.getString("msg"));
                    new AlertDialog.Builder(context)
                            .setTitle(errTitle)
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;
                    int bookingsFound = 0;

                    if (destination.equals("DELETEBOOKING")) {
                        // display msg booking deleted successfully
                        new AlertDialog.Builder(context)
                                .setTitle("Cancel Class Booking")
                                .setMessage("The booking was cancelled successfully")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // restart activity to refresh the booking list
                                        finish();
                                        Intent i = new Intent(context, ClassBookings.class);
                                        i.putExtra("userID", userID);
                                        i.putExtra("accountType", accountType);
                                        startActivity(i);
                                    }
                                })
                                .show();
                    } else if (destination.equals("LISTCLASSES")) {
                        if (jo.getString("status").equals("OK")) {
                            jaData = jo.getJSONArray("data");
                            bookingsFound = jaData.length();

                            JSONObject joData;
                            for (int row = 0; row < bookingsFound; row++) {

                                joData = jaData.getJSONObject(row);
                                classBookingID = joData.getInt("classBookingID");
                                String dateStr = joData.getString("startTime");
                                SimpleDateFormat curFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date dateObj = curFormat.parse(dateStr);
                                SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                classDateTime = newFormat.format(dateObj);
                                classID = joData.getInt("classID");
                                className = joData.getString("name");
                                classTrainerID = joData.getInt("tId");
                                classTrainerName = joData.getString("displayName");

                                // create header row
                                // TO DO

                                // set layout for booked class rows
                                LinearLayout LLC = new LinearLayout(this);
                                LLC.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout.LayoutParams paramsC = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                paramsC.setMargins(10, 10, 10, 10);
                                LLC.setLayoutParams(paramsC);
                                final TextView rowTextView = new TextView(this);
                                rowTextView.setLayoutParams(paramsC);
                                LinearLayout layoutC;

                                // CREATE DYNAMIC CLASS ROWS WITH CANCEL BUTTON
                                // PLACE class and CANCEL button on same line
                                // clicking Cancel button opens a confirmation dialog and then removes the user from the class enrolment table
                                rowTextView.setText(classDateTime + " : " + className);
                                rowTextView.setTag(classBookingID);

                                Button btnCancelClass = new Button(this);
                                btnCancelClass.setText("Cancel");
                                // set tags so that relevant data is passed to the database to remove the user from the class
                                btnCancelClass.setTag(classBookingID);

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
                                                        String data = "classes.php?arg1=dcb&arg2=" + btnCancelClass.getTag();

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
                                LLC.addView(rowTextView);
                                LLC.addView(btnCancelClass);
                                // add the textview to the linearlayout
                                layoutC = (LinearLayout) findViewById(R.id.classBookingsLayout);
                                layoutC.addView(LLC);
                            }

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
//    }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the Classes activity
        finish();
        Intent i = new Intent(context,Classes.class);
        i.putExtra("userID",userID);
        i.putExtra("accountType",accountType);
        startActivity (i);
    }

}
