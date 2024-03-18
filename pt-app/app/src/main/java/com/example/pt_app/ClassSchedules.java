package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClassSchedules extends AppCompatActivity  implements AsyncResponse {
    int userID, classTimeslotID, classID,classTrainerID;
    String className, classTrainerName, classDateTime;

    AsyncResponse asyncResponse;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedules);

        asyncResponse = this;
        context = this;

        // TO DO CHECK SESSION DATA AND GET userID

        userID = 99999;

        // SHOW all current classes for the logged in user
        String data = "classes.php?arg1=gtt&arg2=" + userID;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"LISTSCHEDULES");

    }

    //Get the result of async process
    public void processFinish(String result, String destination){

        try {
            String errTitle="";

            if (destination.equals("LISTSCHEDULES")) {
                errTitle = "Error Retrieving Trainer Class Schedules";
            } else if (destination.equals("DELETESCHEDULE")) {
                errTitle = "Error Deleting Schedule";
            }

            // CONVERT RESULT STRING TO JSON OBJECT
            JSONObject jo = null;
            jo = new JSONObject(result);

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
                    int schedulesFound = 0;

                    if (destination.equals("DELETESCHEDULE")) {
                        // display msg booking deleted successfully
                        new AlertDialog.Builder(context)
                                .setTitle("Cancel Class Schedule")
                                .setMessage("The schedule was cancelled successfully")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", null)
                                .show();
                        // restart activity to refresh the booking list
                        finish();
                        Intent i = new Intent(context,ClassSchedules.class);
                        startActivity (i);
                    } else if (destination.equals("LISTSCHEDULES")) {
                        if (jo.getString("status").equals("OK")) {
                            jaData = jo.getJSONArray("data");
                            schedulesFound = jaData.length();

                            JSONObject joData;
                            for (int row = 0; row < schedulesFound; row++) {

                                joData = jaData.getJSONObject(row);
                                classTimeslotID = joData.getInt("timeslotID");
                                String dateStr = joData.getString("startTime");
                                SimpleDateFormat curFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date dateObj = curFormat.parse(dateStr);
                                SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                classDateTime =  newFormat.format(dateObj);
                                classID = joData.getInt("classID");
                                className = joData.getString("name");
                                classTrainerID = joData.getInt("trainerID");
                                classTrainerName = joData.getString("displayName");

                                // create header row
                                //***************************************TO DO




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
                                // ******* PLACE class and CANCEL button on same line
                                // clicking Cancel button opens a confirmation dialog and then removes the user from the class enrolment table
                                rowTextView.setText(classDateTime + " : " + className);
                                rowTextView.setTag(classTimeslotID);

                                Button btnCancelTimeslot = new Button(this);
                                btnCancelTimeslot.setText("Cancel");
                                // set tags so that relevant data is passed to the database to remove the timeslot
                                btnCancelTimeslot.setTag(classTimeslotID);

                                btnCancelTimeslot.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // OPEN DIALOG BOX TO CONFIRM THE USER WISHES TO CANCEL THE TIMESLOT AND THEN UPDATE DATABASE IF SO
                                        new AlertDialog.Builder(context)
                                                .setTitle("Cancel Scheduled Class Timeslot?")
                                                .setMessage("Are you sure you wish to cancel this class timeslot ?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                        // Remove user from the class enrolment table
                                                        String data = "classes.php?arg1=dct&arg2=" + btnCancelTimeslot.getTag();

                                                        //Create new database connection
                                                        ServerConnection serverConnection = new ServerConnection();
                                                        //Setup response value
                                                        serverConnection.delegate = asyncResponse;
                                                        //Send data to server
                                                        serverConnection.execute(data, "DELETESCHEDULE");

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
                                LLC.addView(btnCancelTimeslot);
                                // add the textview to the linearlayout
                                layoutC = (LinearLayout) findViewById(R.id.classSchedulesLayout);
                                layoutC.addView(LLC);
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            new AlertDialog.Builder(context)
                    .setTitle("Class Schedules - Serious Error")
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
        Intent i = new Intent(context,Classes.class);
        startActivity (i);
    }

}