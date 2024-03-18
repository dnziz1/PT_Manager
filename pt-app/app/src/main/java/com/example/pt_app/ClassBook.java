package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClassBook extends AppCompatActivity implements AsyncResponse {
    int userID,passedClassID,passedDuration,passedTrainerID,timeslotID,spTimeslotsPos;
    String userType,passedClassName,passedClassNotes,passedTrainerName;
    Spinner spTimeslots;
    ArrayList<ClassBookTimeslotModel> arrTimeslots;
    ClassBookTimeslotAdapter spTimeslotsAdapter ;
    Button btnCancel,btnBook;
    AsyncResponse asyncResponse;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_book);
        asyncResponse = this;
        context = this;

        // TO DO: GET USERID AND ACCOUNT TYPE FROM SHARED PREFERENCES
        userID = 99999;
        userType = "TRAINER";

        // Store passed in variables
        Intent intent = getIntent();
        passedClassID = intent.getIntExtra("CLASSID",0);
        passedClassName = intent.getStringExtra("CLASSNAME");
        passedDuration = intent.getIntExtra("DURATION",0);
        passedClassNotes = intent.getStringExtra("NOTES");
        passedTrainerID = intent.getIntExtra("TRAINERID",0);
        passedTrainerName = intent.getStringExtra("TRAINERNAME");

        btnCancel = findViewById(R.id.classBookCancelBtn);
        btnBook = findViewById(R.id.classBookBookBtn);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close activity and return to the Class info activity
                Intent i = new Intent(context,ClassInfo.class);
                i.putExtra("CLASSID",passedClassID);
                i.putExtra("CLASSNAME",passedClassName);
                i.putExtra("DURATION",passedDuration);
                i.putExtra("NOTES",passedClassNotes);
                i.putExtra("TRAINERID",passedTrainerID);
                i.putExtra("TRAINERNAME",passedTrainerName);
                startActivity(i);
                finish();
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check the number of bookings hasn't reached maxOccupancy and if not add booking to the database

                // check that a class has been selected
                if (spTimeslotsPos == 0) {
                    // display error message
                    new AlertDialog.Builder(context)
                            .setTitle("Create Class Booking")
                            .setMessage("You have not selected a timeslot")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();

                    spTimeslots.requestFocus();
                    return;
                }

                //open dialog to confirm user wishes to book this class
                new AlertDialog.Builder(context)
                        .setTitle("Create Class Booking?")
                        .setMessage("Are you sure you wish to book a class with this trainer ?\n If so then the booking will be made if there is a free space")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                // set up PHP data
                                String data = "classes.php?arg1=icb&arg2=" + timeslotID + "&arg3=" + userID;

                                //Create new database connection
                                ServerConnection serverConnection = new ServerConnection();
                                //Setup response value
                                serverConnection.delegate = asyncResponse;
                                //Send data to server
                                serverConnection.execute(data,"INSERTBOOKING");
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

        arrTimeslots = new ArrayList<ClassBookTimeslotModel>();
        spTimeslots = findViewById(R.id.classBookTimeslotID);
        spTimeslotsAdapter = new ClassBookTimeslotAdapter(this, arrTimeslots);
        spTimeslots.setAdapter(spTimeslotsAdapter);

        spTimeslots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                spTimeslotsPos = spTimeslots.getSelectedItemPosition();

                timeslotID = Integer.parseInt(((TextView)view.findViewById(R.id.rClassBookTimeslotID)).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Get available timeslots
        // set up PHP data
        String data = "classes.php?arg1=gavct&arg2=" + passedClassID + "&arg3=" + passedTrainerID;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"spinTimeslots");
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
                            .setTitle("Error Creating Class Booking")
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;
                    int timeslotsFound = 0;

                    if (destination.equals("spinTimeslots")) {
                        if (jo.getString("status").equals("OK")) {
                            jaData = jo.getJSONArray("data");
                            timeslotsFound = jaData.length();
                        }

                        PopulateTimeslotSpinner(jaData);
                    } else if (destination.equals("INSERTBOOKING")) {
                        // display success message
                        new AlertDialog.Builder(context)
                                .setTitle("Create Class Booking")
                                .setMessage(jo.getString("msg"))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //restart the ClassBook activity to update the available timeslots
                                        Intent i = new Intent(context, ClassBook.class);
                                        i.putExtra("CLASSID",passedClassID);
                                        i.putExtra("CLASSNAME",passedClassName);
                                        i.putExtra("DURATION",passedDuration);
                                        i.putExtra("NOTES",passedClassNotes);
                                        i.putExtra("TRAINERID",passedTrainerID);
                                        i.putExtra("TRAINERNAME",passedTrainerName);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .show();

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


    private void PopulateTimeslotSpinner (JSONArray ja) throws ParseException {
        // Populate Timeslot spinner with db data

        arrTimeslots.clear();
        // add default All option
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date dtStartTime = sdf.parse("00/00/0000 00:00");
        arrTimeslots.add(new ClassBookTimeslotModel(0,0,dtStartTime));
        // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
        if (!(ja == null)) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    int timeslotID = Integer.parseInt(jo.getString("timeslotID"));
                    int scheduleID = Integer.parseInt(jo.getString("scheduleID"));
                    Date startTime = sdfDB.parse(jo.getString("startTime"));
                    ClassBookTimeslotModel timeslotInfo = new ClassBookTimeslotModel(timeslotID, scheduleID,startTime);
                    arrTimeslots.add(timeslotInfo);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        spTimeslotsAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the Class info activity
        Intent i = new Intent(context,ClassInfo.class);
        i.putExtra("CLASSID",passedClassID);
        i.putExtra("CLASSNAME",passedClassName);
        i.putExtra("DURATION",passedDuration);
        i.putExtra("NOTES",passedClassNotes);
        i.putExtra("TRAINERID",passedTrainerID);
        i.putExtra("TRAINERNAME",passedTrainerName);
        startActivity(i);
        finish();
    }

}