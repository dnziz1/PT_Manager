package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

public class ProgramEventCreateEdit extends AppCompatActivity implements AsyncResponse {

    AsyncResponse asyncResponse;
    Context context;
    Button cancelBtn, saveBtn, deleteBtn;
    // create array of Strings
    // and store name of courses
    String[] workouts = {"Data structures",
            "Interview prep", "Algorithms",
            "DSA with java", "OS"};

    Spinner spWorkoutTypes;
    TextView tvEventNotes;
    ArrayList<ProgramEventWorkoutModel> arrWorkoutTypes;
    ProgramEventWorkoutAdapter spWorkoutTypesAdapter ;

    int passedProgID,passedDayID,passedEventID,passedWorkoutTypeID;
    String passedMode,passedWorkoutName,passedWorkoutMuscleGroup,passedWorkoutLevel,passedWorkoutEquipment,passedEventNotes;
    boolean bolCreateMode;

    int workoutID;
    String workoutName,workoutMuscleGroup,workoutLevel,workoutEquipment,eventNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_event_create_edit);
        asyncResponse = this;
        context = this;

        // Store passed in variables
        Intent intent = getIntent();
        passedMode = intent.getStringExtra("MODE");
        passedProgID = intent.getIntExtra("PROGID",0);
        passedDayID = intent.getIntExtra("DAYID",0);
        passedEventID = intent.getIntExtra("EVENTID",0);
        passedWorkoutTypeID = intent.getIntExtra("WORKOUTID",0);
        passedWorkoutName = intent.getStringExtra("WORKOUTNAME");
        //passedWorkoutMuscleGroup = intent.getStringExtra("WORKOUTMUSCLEGROUP");
        //passedWorkoutLevel = intent.getStringExtra("WORKOUTLEVEL");
        //passedWorkoutEquipment = intent.getStringExtra("WORKOUTEQUIPMENT");
        passedEventNotes = intent.getStringExtra("EVENTNOTES");
        bolCreateMode = true; // set default mode as creating a new program

        // Find out if creating or editing a program event and set the screen accordingly
        if (passedMode.equals("EDIT")) bolCreateMode = false;

        tvEventNotes = findViewById(R.id.progEventNotes);

        arrWorkoutTypes = new ArrayList<ProgramEventWorkoutModel>();
        spWorkoutTypes = findViewById(R.id.progEventWorkoutSpinner);
        spWorkoutTypesAdapter = new ProgramEventWorkoutAdapter(this, arrWorkoutTypes);
        spWorkoutTypes.setAdapter(spWorkoutTypesAdapter);

        // Take the instance of Spinner and apply OnItemSelectedListener on it which tells which item of spinner is clicked
        spWorkoutTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spWorkoutTypes.getSelectedItemPosition();

                String workoutTypeID = ((TextView)view.findViewById(R.id.rProgEventWorkoutID)).getText().toString();
                String workoutName = ((TextView)view.findViewById(R.id.rProgEventWorkoutName)).getText().toString();

                Toast.makeText(getApplicationContext(),
                        "WorkoutTypeID : " + workoutTypeID +"\n"
                                +"Name : " + workoutName +"\n", Toast.LENGTH_SHORT).show();
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

        saveBtn = findViewById(R.id.progEventSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //progID = passedProgID;
                //workoutID = Integer.parseInt(((TextView)view.findViewById(R.id.rProgEventWorkoutID)).getText().toString());
                //workoutName = ((TextView)view.findViewById(R.id.rProgEventWorkoutName)).getText().toString();
                //eventNotes = tvEventNotes.getText().toString();

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

        deleteBtn = findViewById(R.id.progEventDelete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //progID = passedProgID;
                //workoutID = tvWorkoutID.getText().toString();
                //eventNotes = tvEventNotes.getText().toString();

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
                            finish();
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

        cancelBtn = findViewById(R.id.progEventCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close activity and return to the day planner
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void SaveData(String ID) {

        // Get selected workout type from the spinner
        int selectedWorkoutPos = spWorkoutTypes.getSelectedItemPosition();
        ProgramEventWorkoutModel mSelectedWorkout = arrWorkoutTypes.get(selectedWorkoutPos);
        workoutID = mSelectedWorkout.getWorkoutID();
        workoutName = mSelectedWorkout.getName();
        //workoutMuscleGroup = mSelectedWorkout.getMuscleGroup();
        //workoutLevel = mSelectedWorkout.getLevel();
        //workoutEquipment = mSelectedWorkout.getEquipment();
        eventNotes = tvEventNotes.getText().toString();
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

        //finish();
    }

    //Get the result of async process
    public void processFinish(String result, String destination) {

        // CONVERT RESULT STRING TO JSON ARRAY
        JSONObject jo = null;
        try {
            jo = new JSONObject(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (jo.length() > 0) {
            try {
                if (jo.getString("status").equals("Error")) {
                    Log.d("Error", jo.getString("msg"));

                    String errTitle;
                    if (destination.equals("DELETEEVENT")) {
                        errTitle = "Error Deleting Program Event";
                    } else{
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
                            passedMode = "EDIT";
                            passedWorkoutTypeID = workoutID;
                            passedWorkoutName = workoutName;
                            //passedWorkoutMuscleGroup = workoutMuscleGroup;
                            //passedWorkoutLevel = workoutLevel;
                            //passedWorkoutEquipment = workoutEquipment;
                            passedEventNotes = eventNotes;
                            bolCreateMode = false;

                            if (destination.equals("CREATEEVENT")) {
                                passedEventID = jo.getInt("eventID");

                                new AlertDialog.Builder(context)
                                        .setTitle("Create Program Event")
                                        .setMessage("The event was created successfully")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", null)
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
                                        .setPositiveButton("OK", null)
                                        .show();
                                        finish();
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void PopulateWorkoutTypeSpinner (JSONArray ja) {
        // Populate Workout type spinner with db data

        arrWorkoutTypes.clear();
        // add default All option
        //arrWorkoutTypes.add(new ProgramEventWorkoutTypeModel(0,"ANY"));
        // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
        if (!(ja == null)) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    int workoutTypeID = Integer.parseInt(jo.getString("workoutID"));
                    String muscleGroup = jo.getString("muscleGroup");
                    String workoutName = jo.getString("exerciseName");
                    String level = jo.getString("level");
                    String equipment = jo.getString("equipment");
                    ProgramEventWorkoutModel workoutType = new ProgramEventWorkoutModel(workoutTypeID, muscleGroup, workoutName, level, equipment);
                    arrWorkoutTypes.add(workoutType);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
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

            if (indexFound >=0) {
                spWorkoutTypes.setSelection(indexFound);
            }

            tvEventNotes.setText(passedEventNotes);
        }

    }

}