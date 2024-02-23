package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class ProgramDayPlanner extends AppCompatActivity implements AsyncResponse {

    int passedProgID, passedProgDuration, passedTrainerID;
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
        //passedMode = intent.getStringExtra("MODE");
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

        //if (!(passedTrainerID == logged in userid)) bolViewOnly = true;

        //passedProgNotes = intent.getStringExtra("NOTES");
        //bolCreateMode = true; // set default mode as creating a new program

            //TextView tvProgName = (TextView) findViewById(R.id.progCreateName);
        //
        // SHOW all Program Days and any events

        String data = "programs.php?arg1=gpe&arg2=" + passedProgID;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"");

    }

    // Refresh activity when coming back to it to show any new events(workouts/tasks etc)
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    //Get the result of async process
    public void processFinish(String result, String destination){

        // CONVERT RESULT STRING TO JSON ARRAY
        JSONArray ja = null;
        JSONObject jo = null;
        try {
            //ja = new JSONArray(result);
            jo = new JSONObject(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (jo.length() > 0) {
            try {
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

                    if (jo.getString("status").equals("OK")) {
                        jaData = jo.getJSONArray("data");
                    }

//                    if (destination.equals("events")) {
//                        PopulateTrainerSpinner(jaData);
//                    } else if (destination.equals("listPrograms")) {
//                        PopulateProgramListView(jaData);
//                    }

                    //String createTime = "";

                    for (int day = 1; day <= passedProgDuration; day++) {

                        LinearLayout LLH = new LinearLayout(this);
                        LLH.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(10, 10, 10, 10);
                        LLH.setLayoutParams(params);

                        // create a new textview
                        final TextView rowTextView = new TextView(this);
                        rowTextView.setLayoutParams(params);

                        // ******* PLACE Day and Add event on same line
                        // clicking Add event opens pop up window to choose a workout, task or custom text

                        // set some properties of rowTextView
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

                        // add day and Add event button on the same row
                        LLH.addView(rowTextView);
                        LLH.addView(btnAddEvent);
                        // add the textview to the linearlayout
                        LinearLayout layout = (LinearLayout) findViewById(R.id.programDayPlanLayout);
                        layout.addView(LLH);

                        btnAddEvent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // OPEN EDIT Program Event Screen for the selected program
                                Intent i = new Intent(context, ProgramEventCreateEdit.class);
                                i.putExtra("MODE","CREATE");
                                i.putExtra("PROGID",passedProgID);

                                // Get the day from the button tag
                                i.putExtra("DAYID",String.valueOf(btnAddEvent.getTag()));
                                startActivity(i);
                            }
                        });

                        // ITERATE THROUGH JSONArray AND UPDATE ACTIVITY FIELDS
                        int dayID, eventID, workoutID;
                        String workoutName, muscleGroup, level, equipment, eventNotes;

                        // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
                        if (!(jaData == null)) {
                            for (int j = 0; j < jaData.length(); j++) {
                                //try {
                                jo = jaData.getJSONObject(j);
                                // RETRIEVE EACH JSON OBJECT'S FIELDS

                                dayID = jo.getInt("dayID");

                                if (dayID == day) {
                                    eventID = jo.getInt("eventID");
                                    workoutID = jo.getInt("workoutID");
                                    workoutName = jo.getString("exerciseName");
                                    muscleGroup = jo.getString("muscleGroup");
                                    level = jo.getString("level");
                                    equipment = jo.getString("equipment");
                                    eventNotes = jo.getString("notes");

                                    // CREATE DYNAMIC EVENT ROWS WITH EDIT BUTTON
                                    // build rowTextView from event and notes
                                    LinearLayout LLE = new LinearLayout(this);
                                    LLE.setOrientation(LinearLayout.HORIZONTAL);
                                    LinearLayout.LayoutParams paramsE = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    paramsE.setMargins(10, 10, 10, 10);
                                    LLE.setLayoutParams(paramsE);

                                    // create a new textview
                                    final TextView rowTextViewE = new TextView(this);
                                    rowTextViewE.setLayoutParams(paramsE);

                                    // ******* PLACE event and EDIT button on same line
                                    // clicking Edit event opens pop up window to amend workout, task or custom text
                                    //rowTextViewE.setText(eventID + " : " + workoutID + " : " + workoutName + " : "  + muscleGroup + " : "  + level + " : " + eventNotes);
                                    rowTextViewE.setText(eventID + " : " + workoutID + " : " + workoutName);

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

                                    // add day and Add event button on the same row
                                    LLE.addView(rowTextViewE);
                                    LLE.addView(btnEditEvent);
                                    // add the textview to the linearlayout
                                    LinearLayout layoutE = (LinearLayout) findViewById(R.id.programDayPlanLayout);
                                    layoutE.addView(LLE);

                                    btnEditEvent.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Context mContext = MyApp.getAppContext();
                                            //Intent iEditEvent = new Intent(getApplicationContext(), ProgramEventCreate.class);
                                            //Intent iEditEvent = new Intent(mContext, ProgramEventCreate.class);
                                            //startActivity(iEditEvent);
                                            // OPEN EDIT Program Screen for the selected program
                                            Intent i = new Intent(context, ProgramEventCreateEdit.class);
                                            i.putExtra("MODE","EDIT");
                                            i.putExtra("PROGID",passedProgID);
                                            String[] data = btnEditEvent.getTag().toString().split(";",-1);
                                            i.putExtra("DAYID",Integer.parseInt(data[0]));
                                            i.putExtra("EVENTID",Integer.parseInt(data[1]));
                                            i.putExtra("WORKOUTID",Integer.parseInt(data[2]));
                                            i.putExtra("WORKOUTNAME",data[3]);
                                            i.putExtra("EVENTNOTES",data[4]);
                                            startActivity(i);
                                        }
                                    });
                                }
                                //} catch(JSONException e){
                                //    throw new RuntimeException(e);
                                //}
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }


}