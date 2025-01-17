package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.Value;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import android.preference.PreferenceManager;

public class ProgramEventCreateEdit extends AppCompatActivity implements AsyncResponse {

    AsyncResponse asyncResponse;
    Context context;
    Button cancelBtn, saveBtn, deleteBtn;
    Spinner spWorkoutTypes;
    TextView tvEventNotes;
    ArrayList<ProgramEventWorkoutModel> arrWorkoutTypes;
    ProgramEventWorkoutAdapter spWorkoutTypesAdapter ;

    int passedProgID,passedProgDuration,passedDayID,passedEventID,passedTrainerID, passedWorkoutTypeID;
    String passedMode,passedProgName, passedProgNotes,passedWorkoutName,passedWorkoutMuscleGroup,passedWorkoutLevel,passedWorkoutEquipment,passedEventNotes;
    boolean bolCreateMode;

    int workoutID,userID;
    String workoutName,workoutMuscleGroup,workoutLevel,workoutEquipment,eventNotes,accountType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_event_create_edit);
        asyncResponse = this;
        context = this;

        // Store passed in variables
        Intent intent = getIntent();
        passedMode = intent.getStringExtra("MODE");
        userID = intent.getIntExtra("userID",0);
        accountType = intent.getStringExtra("accountType");
        passedProgID = intent.getIntExtra("PROGID",0);
        passedProgName = intent.getStringExtra("PROGNAME");
        passedProgDuration = intent.getIntExtra("DURATION",0);
        passedProgNotes = intent.getStringExtra("NOTES");
        passedDayID = intent.getIntExtra("DAYID",0);
        passedEventID = intent.getIntExtra("EVENTID",0);
        passedTrainerID = intent.getIntExtra("TRAINERID",0);
        passedWorkoutTypeID = intent.getIntExtra("WORKOUTID",0);
        passedWorkoutName = intent.getStringExtra("WORKOUTNAME");
        //passedWorkoutMuscleGroup = intent.getStringExtra("WORKOUTMUSCLEGROUP");
        //passedWorkoutLevel = intent.getStringExtra("WORKOUTLEVEL");
        //passedWorkoutEquipment = intent.getStringExtra("WORKOUTEQUIPMENT");
        passedEventNotes = intent.getStringExtra("EVENTNOTES");
        bolCreateMode = true; // set default mode as creating a new program

        spWorkoutTypes = findViewById(R.id.progEventWorkoutSpinner);
        tvEventNotes = findViewById(R.id.progEventNotes);
        deleteBtn = findViewById(R.id.progEventDelete);
        saveBtn = findViewById(R.id.progEventSave);

        if (!(passedTrainerID == userID && accountType.equals("TRAINER"))) {
            //spWorkoutTypes.setEnabled(false);
            spWorkoutTypes.setFocusable(false);
            spWorkoutTypes.setActivated(false);
            //spWorkoutTypes.setInputType(InputType.TYPE_NULL);
            //tvEventNotes.setEnabled(false);
            tvEventNotes.setFocusable(false);
            tvEventNotes.setActivated(false);
            tvEventNotes.setInputType(InputType.TYPE_NULL);
            deleteBtn.setEnabled(false);
            deleteBtn.setFocusable(false);
            deleteBtn.setActivated(false);
            deleteBtn.setInputType(InputType.TYPE_NULL);
            saveBtn.setEnabled(false);
            saveBtn.setFocusable(false);
            saveBtn.setActivated(false);
            saveBtn.setInputType(InputType.TYPE_NULL);
        }

        // Find out if creating or editing a program event and set the screen accordingly
        if (passedMode.equals("EDIT")) bolCreateMode = false;

        arrWorkoutTypes = new ArrayList<ProgramEventWorkoutModel>();
        spWorkoutTypesAdapter = new ProgramEventWorkoutAdapter(this, arrWorkoutTypes);
        spWorkoutTypes.setAdapter(spWorkoutTypesAdapter);

        // Take the instance of Spinner and apply OnItemSelectedListener on it which tells which item of spinner is clicked
        spWorkoutTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get all workout types from the database to build the workout types spinner
        String data = "programs.php?arg1=gaw";

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"spinWorkoutTypes");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get selected workout type from the spinner
                int selectedWorkoutPos = spWorkoutTypes.getSelectedItemPosition();
                ProgramEventWorkoutModel mSelectedWorkout = arrWorkoutTypes.get(selectedWorkoutPos);
                workoutID = mSelectedWorkout.getWorkoutID();
                workoutName = mSelectedWorkout.getName();
                //workoutMuscleGroup = mSelectedWorkout.getMuscleGroup();
                //workoutLevel = mSelectedWorkout.getLevel();
                //workoutEquipment = mSelectedWorkout.getEquipment();
                eventNotes = tvEventNotes.getText().toString();

                if (bolCreateMode) {
                    SaveData("CREATEEVENT");
                } else {
                    if ((workoutID == passedWorkoutTypeID) && (eventNotes == passedEventNotes)) {
                        // display msg Updated successfully
                        new AlertDialog.Builder(context)
                                .setTitle("Update Program Event")
                                .setMessage("The event was updated successfully")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        // Ask user to confirm they wish to update the event
                        //
                        new AlertDialog.Builder(context)
                                .setTitle("Update Program Event?")
                                .setMessage("Are you sure you wish to update this event with the chosen data ?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        SaveData("UPDATEEVENT");
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
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Ask user to confirm they wish to delete the event
                //
                new AlertDialog.Builder(context)
                   .setTitle("Delete Program Event?")
                   .setMessage("Are you sure you wish to delete this event ?")
                   .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            SaveData("DELETEEVENT");
                            //finish();
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

        if (bolCreateMode) {
            deleteBtn.setEnabled(false);
            deleteBtn.setFocusable(false);
            deleteBtn.setActivated(false);
        }

        cancelBtn = findViewById(R.id.progEventCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close activity and return to the day planner
                Intent i = new Intent(context, ProgramDayPlanner.class);
                i.putExtra("MODE","EDIT");
                i.putExtra("userID",userID);
                i.putExtra("accountType",accountType);
                i.putExtra("PROGID",passedProgID);
                i.putExtra("PROGNAME", passedProgName);
                i.putExtra("DURATION",passedProgDuration);
                i.putExtra("NOTES", passedProgNotes);
                i.putExtra("TRAINERID",passedTrainerID);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the day planner
        Intent i = new Intent(context, ProgramDayPlanner.class);
        i.putExtra("MODE","EDIT");
        i.putExtra("userID",userID);
        i.putExtra("accountType",accountType);
        i.putExtra("PROGID",passedProgID);
        i.putExtra("PROGNAME", passedProgName);
        i.putExtra("DURATION",passedProgDuration);
        i.putExtra("NOTES", passedProgNotes);
        i.putExtra("TRAINERID",passedTrainerID);
        startActivity(i);
        finish();
    }

    public void SaveData(String ID) {

        String data;

        if (ID.equals("DELETEEVENT")) {
            // Delete event
            data = "programs.php?arg1=dpe&arg2=" + passedProgID + "&arg3=" + passedDayID + "&arg4=" + passedEventID;
        } else {
            // Save new event/changes to an existing event
            if (bolCreateMode) {
                // Create event
                data = "programs.php?arg1=ipe&arg2=" + passedProgID + "&arg3=" + passedDayID + "&arg4=" + workoutID + "&arg5=" + eventNotes;
            } else {
                // Update event
                data = "programs.php?arg1=upe&arg2=" + passedProgID + "&arg3=" + passedDayID + "&arg4=" + passedEventID + "&arg5=" + workoutID + "&arg6=" + eventNotes;
            }
        }

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data, ID);

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

            if (jo.length() > 0) {
                if (jo.getString("status").equals("Error")) {
                    Log.d("Error", jo.getString("msg"));

                    String errTitle;
                    if (destination.equals("DELETEEVENT")) {
                        errTitle = "Error Deleting Program Event";
                    } else {
                        if (bolCreateMode) {
                            errTitle = "Error Creating Program Event";
                        } else {
                            errTitle = "Error Updating Program Event";
                        }
                    }
                    new AlertDialog.Builder(context)
                            .setTitle(errTitle)
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;

                    if (jo.getString("status").equals("OK")) {

                        if (destination.equals("spinWorkoutTypes")) {
                            // Populate the workout type spinner with the data
                            jaData = jo.getJSONArray("data");
                            PopulateWorkoutTypeSpinner(jaData);
                        } else {
                            // Reset screen as edit mode
                            if (bolCreateMode) {
                                // retrieve newly created eventID from DB
                                passedEventID = Integer.parseInt(jo.getString("eventID"));
                                passedMode = "EDIT";
                                bolCreateMode = false;
                            }
                            passedWorkoutTypeID = workoutID;
                            passedWorkoutName = workoutName;
                            //passedWorkoutMuscleGroup = workoutMuscleGroup;
                            //passedWorkoutLevel = workoutLevel;
                            //passedWorkoutEquipment = workoutEquipment;
                            passedEventNotes = eventNotes;

                            if (destination.equals("CREATEEVENT")) {
                                new AlertDialog.Builder(context)
                                        .setTitle("Create Program Event")
                                        .setMessage("The event was created successfully")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // restart activity in edit mode
                                                Intent i = getIntent();
                                                i.putExtra("MODE","EDIT");
                                                i.putExtra("userID",userID);
                                                i.putExtra("accountType",accountType);
                                                i.putExtra("PROGID",passedProgID);
                                                i.putExtra("PROGNAME", passedProgName);
                                                i.putExtra("DURATION",passedProgDuration);
                                                i.putExtra("NOTES", passedProgNotes);
                                                i.putExtra("DAYID",passedDayID);
                                                i.putExtra("EVENTID",passedEventID);
                                                i.putExtra("TRAINERID",passedTrainerID);
                                                i.putExtra("WORKOUTID",passedWorkoutTypeID);
                                                i.putExtra("WORKOUTNAME",passedWorkoutName);
                                                i.putExtra("EVENTNOTES",passedEventNotes);
                                                finish();
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                            } else if (destination.equals("UPDATEEVENT")) {
                                new AlertDialog.Builder(context)
                                        .setTitle("Update Program Event")
                                        .setMessage("The event was updated successfully")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", null)
                                        .show();
                            } else if (destination.equals("DELETEEVENT")) {
                                new AlertDialog.Builder(this)
                                        .setTitle("Delete Program Event")
                                        .setMessage("The event was successfully deleted")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //close activity and return to the day planner
                                                Intent i = new Intent(context, ProgramDayPlanner.class);
                                                i.putExtra("MODE","EDIT");
                                                i.putExtra("userID",userID);
                                                i.putExtra("accountType",accountType);
                                                i.putExtra("PROGID",passedProgID);
                                                i.putExtra("PROGNAME", passedProgName);
                                                i.putExtra("DURATION",passedProgDuration);
                                                i.putExtra("NOTES", passedProgNotes);
                                                i.putExtra("TRAINERID",passedTrainerID);
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            new AlertDialog.Builder(context)
                    .setTitle("Program Event Create/Edit - Serious Error")
                    .setMessage(e.getMessage())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void PopulateWorkoutTypeSpinner (JSONArray ja) {
        // Populate Workout type spinner with db data

        try {
            arrWorkoutTypes.clear();
            spWorkoutTypesAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner

            JSONObject jo;
            int workoutTypeID;
            String muscleGroup, workoutName,level,equipment;
            ProgramEventWorkoutModel workoutType;

            // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
            if (!(ja == null)) {
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    workoutTypeID = jo.getInt("workoutID");
                    muscleGroup = jo.getString("muscleGroup");
                    workoutName = jo.getString("exerciseName");
                    level = jo.getString("level");
                    equipment = jo.getString("equipment");
                    workoutType = new ProgramEventWorkoutModel(workoutTypeID, muscleGroup, workoutName, level, equipment);
                    arrWorkoutTypes.add(workoutType);
                }
            }

            spWorkoutTypesAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner

            if (!bolCreateMode) {
                // SET THE SELECTED ITEM IN THE WORKOUT SPINNER TO THE PASSED IN VALUE
                ProgramEventWorkoutModel ewm;
                int indexFound = -1;

                for (int i = 0; i < arrWorkoutTypes.size(); i++) {
                    ewm = arrWorkoutTypes.get(i);

                    if (ewm.workoutID == passedWorkoutTypeID) {
                        indexFound = i;
                    }
                }

                if (indexFound >= 0) {
                    spWorkoutTypes.setSelection(indexFound);
                }

                tvEventNotes.setText(passedEventNotes);
            }
        } catch (Exception e) {
            new AlertDialog.Builder(context)
                .setTitle("Program Event Create/Edit - Serious Error")
                .setMessage(e.getMessage())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                })
                .show();
        }

    }

}