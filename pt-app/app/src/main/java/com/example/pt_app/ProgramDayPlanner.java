package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ProgramDayPlanner extends AppCompatActivity implements AsyncResponse {

    int passedProgID, passedProgDuration, passedTrainerID,userID;
    String passedProgName;
    boolean bolViewOnly;
    AsyncResponse asyncResponse;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_day_planner);
        asyncResponse = this;
        context = this;

        // Store passed in variables
        Intent intent = getIntent();
        passedProgID = intent.getIntExtra("PROGID",0);
        passedProgName = intent.getStringExtra("PROGNAME");
        passedProgDuration = intent.getIntExtra("DURATION",0);
        passedTrainerID = intent.getIntExtra("TRAINERID",0);
        bolViewOnly = false;

        // If user isn't the creator of the program then set screen as view only
        // ***************************************************************
        // ***************************************************************
        // ***************************************************************
        // ***************************************************************
        // ***************************************************************
        // ***************************************************************
        // UNCOMMENT LINE BELOW ONCE GOT SHARED PREFERENCE USERID*********
        // Set initial default id to 99999
        userID = 99999;
        if (!(passedTrainerID == userID)) bolViewOnly = true;

        // SHOW all Program Days and any events
        String data = "programs.php?arg1=gpe&arg2=" + passedProgID;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"");

    }

/*    // Refresh activity when coming back to it to show any new events(workouts/tasks etc)
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("events_changed", false)) {
            prefs.edit().remove("events_changed").apply();
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }
*/
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
                            .setTitle("Error Retrieving Program Events")
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;
                    int eventsFound = 0;

                    if (jo.getString("status").equals("OK")) {
                        jaData = jo.getJSONArray("data");
                        eventsFound = jaData.length();
                    }

                    JSONObject joData;
                    int day = 0, dataDay = 0;
                    int eventNoForDay = 0;
                    int dayID, eventID, workoutID,dayHeadersToAdd;
                    String workoutName, muscleGroup, level, equipment, eventNotes;

                    for (int row = 0; row < eventsFound; row++) {

                        joData = jaData.getJSONObject(row);
                        dataDay = joData.getInt("dayID");
                        dayHeadersToAdd = dataDay-day;
                        // create header rows for any days before the current event day
                        for (int h = 0; h < dayHeadersToAdd; h++) {
                            day++;
                            createDayHeader(day);
                        }

                        // set layout for event rows
                        LinearLayout LLE = new LinearLayout(this);
                        LLE.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams paramsE = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        paramsE.setMargins(10, 10, 10, 10);
                        LLE.setLayoutParams(paramsE);
                        final TextView rowTextViewE = new TextView(this);
                        rowTextViewE.setLayoutParams(paramsE);
                        LinearLayout layoutE;

                        // ITERATE THROUGH JSONArray AND UPDATE ACTIVITY FIELDS
                        eventID = joData.getInt("eventID");
                        workoutID = joData.getInt("workoutID");
                        workoutName = joData.getString("exerciseName");
                        muscleGroup = joData.getString("muscleGroup");
                        level = joData.getString("level");
                        equipment = joData.getString("equipment");
                        eventNotes = joData.getString("notes");

                        // CREATE DYNAMIC EVENT ROWS WITH EDIT BUTTON
                        // ******* PLACE event and EDIT button on same line
                        // clicking Edit event opens pop up window to amend workout, task or custom text
                        //rowTextViewE.setText(eventID + " : " + workoutID + " : " + workoutName + " : "  + muscleGroup + " : "  + level + " : " + eventNotes);
                        rowTextViewE.setText(eventID + " : " + workoutID + " : " + workoutName);
                        rowTextViewE.setTag(day + ";" + eventID + ";" + workoutID + ";" + workoutName + ";" + eventNotes);

                        Button btnEditEvent = new Button(this);
                        btnEditEvent.setText("Edit");
                        // set tags so that dayID and eventID can be passed to the event create/edit activity
                        btnEditEvent.setTag(day + ";" + eventID + ";" + workoutID + ";" + workoutName + ";" + eventNotes);

                        if (bolViewOnly) {
                            btnEditEvent.setEnabled(false);
                            btnEditEvent.setFocusable(false);
                            btnEditEvent.setActivated(false);
                            btnEditEvent.setInputType(InputType.TYPE_NULL);
                        }
                        btnEditEvent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // OPEN EDIT Program Screen for the selected program
                                Intent i = new Intent(context, ProgramEventCreateEdit.class);
                                i.putExtra("MODE", "EDIT");
                                i.putExtra("PROGID", passedProgID);
                                i.putExtra("PROGNAME", passedProgName);
                                i.putExtra("DURATION", passedProgDuration);
                                String[] data = btnEditEvent.getTag().toString().split(";", -1);
                                i.putExtra("DAYID", Integer.parseInt(data[0]));
                                i.putExtra("EVENTID", Integer.parseInt(data[1]));
                                i.putExtra("TRAINERID",passedTrainerID);
                                i.putExtra("WORKOUTID", Integer.parseInt(data[2]));
                                i.putExtra("WORKOUTNAME", data[3]);
                                i.putExtra("EVENTNOTES", data[4]);
                                startActivity(i);
                                finish();
                            }
                        });

                        // add day and Add event button on the same row
                        LLE.addView(rowTextViewE);
                        LLE.addView(btnEditEvent);
                        // add the textview to the linearlayout
                        layoutE = (LinearLayout) findViewById(R.id.programDayPlanLayout);
                        layoutE.addView(LLE);
                    }

                    // create header rows for any days after the last event
                    int headersToDo = passedProgDuration - day;
                    for (int h = 0; h < headersToDo; h++) {
                        day++;
                        createDayHeader(day);
                    }

                }
            }
        } catch (Exception e) {
            new AlertDialog.Builder(context)
                .setTitle("Program Day Planner - Serious Error")
                .setMessage(e.getMessage())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", null)
                .show();
        }
    }

    private void createDayHeader (int day) {

        // set layout for day header rows
        LinearLayout LLH = new LinearLayout(this);
        LLH.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        LLH.setLayoutParams(params);
        final TextView rowTextView = new TextView(this);
        rowTextView.setLayoutParams(params);

        rowTextView.setText("Day " + day);

        Button btnAddEvent = new Button(this);
        btnAddEvent.setText("Add Event");
        btnAddEvent.setTag(day); // set button tag so it can be passed to the event create/edit activity

        if (bolViewOnly) {
            btnAddEvent.setEnabled(false);
            btnAddEvent.setFocusable(false);
            btnAddEvent.setActivated(false);
            btnAddEvent.setInputType(InputType.TYPE_NULL);
        }
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // OPEN EDIT Program Event Screen for the selected program
                Intent i = new Intent(context, ProgramEventCreateEdit.class);
                i.putExtra("MODE","CREATE");
                i.putExtra("PROGID",passedProgID);
                i.putExtra("PROGNAME", passedProgName);
                i.putExtra("DURATION", passedProgDuration);
                i.putExtra("TRAINERID",passedTrainerID);

                // Get the day from the button tag
                i.putExtra("DAYID",Integer.parseInt(String.valueOf(btnAddEvent.getTag())));
                startActivity(i);
                finish();
            }
        });

        // add day and Add event button on the same row
        LLH.addView(rowTextView);
        LLH.addView(btnAddEvent);
        // add the textview to the linearlayout
        LinearLayout layout = (LinearLayout) findViewById(R.id.programDayPlanLayout);
        layout.addView(LLH);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the program create/edit activity
        finish();
    }

}