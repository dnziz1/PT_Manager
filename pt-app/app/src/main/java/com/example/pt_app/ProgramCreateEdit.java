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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgramCreateEdit extends AppCompatActivity implements AsyncResponse {

    AsyncResponse asyncResponse;
    Context context;
    Button btnPlan,btnSave,btnCancel;
    boolean bolCreateMode, bolUpdateConfirmed;
    String progName,progNotes,data;
    int progID,progDuration;
    boolean bolProgDataChanged;

    TextView tvProgName,tvProgDuration,tvProgNotes;

    int passedProgID,passedProgDuration;
    String passedMode,passedProgName,passedProgNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_create_edit);
        asyncResponse = this;
        context = this;

        tvProgName = findViewById(R.id.progCreateName);
        tvProgDuration = findViewById(R.id.progCreateDuration);
        tvProgNotes = findViewById(R.id.progCreateNotes);
        //TextView tvTrainerID = findViewById(session variable trainerID);
        //progName = tvProgName.getText().toString();
        //progDuration = Integer.parseInt(tvProgDuration.getText().toString());
        //progNotes = tvProgNotes.getText().toString();

        // Store passed in variables
        Intent intent = getIntent();
        passedMode = intent.getStringExtra("MODE");
        passedProgID = intent.getIntExtra("PROGID",0);
        passedProgName = intent.getStringExtra("PROGNAME");
        passedProgDuration = intent.getIntExtra("DURATION",0);
        passedProgNotes = intent.getStringExtra("NOTES");
        bolCreateMode = true; // set default mode as creating a new program

        // Find out if creating or editing a program and set the screen accordingly
        if (passedMode.equals("EDIT")) {
            bolCreateMode = false;
            // COPY DATA FROM LIST SCREEN AND POPULATE ACTIVITY VIEWS
            //tvprogramID.setText(progIDToEdit);
            tvProgName.setText(passedProgName);
            tvProgDuration.setText(String.valueOf(passedProgDuration));
            tvProgNotes.setText(passedProgNotes);
        }

        btnSave = findViewById(R.id.progCreateSaveBtn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progName = tvProgName.getText().toString();
                progDuration = Integer.parseInt(tvProgDuration.getText().toString());
                progNotes = tvProgNotes.getText().toString();

                // *******************check valid screen values first
                if (bolCreateMode) {
                    SaveData("CREATEPROG");
                } else {
                    progID = passedProgID;

                    if (progName.equals(passedProgName) && (progDuration == passedProgDuration) && progNotes.equals(passedProgNotes)) {
                        // display msg Updated successfully
                        new AlertDialog.Builder(context)
                                .setTitle("Update Program")
                                .setMessage("The program was updated successfully")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        // If the duration has been changed then any existing event data for days greater than the duration will be deleted
                        // so warn the user
                        new AlertDialog.Builder(context)
                                .setTitle("Update Program?")
                                .setMessage("Are you sure you wish to update this program with the chosen data ?\n WARNING! If the duration has been reduced then any existing event data for days greater than the duration will be deleted!")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        SaveData("UPDATEPROG");
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



/*
                progName = tvProgName.getText().toString();
                progDuration = Integer.parseInt(tvProgDuration.getText().toString());
                progNotes = tvProgNotes.getText().toString();
                bolProgDataChanged = false;

                // If creating a new program then insert program to the database otherwise update existing program
                if (bolCreateMode) {
                    // Create program
                    data = "programs.php?arg1=ip&arg2=" + progName + "&arg3=" + progDuration + "&arg4=" + progNotes;
                } else {
                    // Check if user has changed any of the passed in data
                    if (!progName.equals(passedProgName) || (progDuration != passedProgDuration) || !progNotes.equals(passedProgNotes)) {
                        bolProgDataChanged = true;
                    }

                    data = "programs.php?arg1=up&arg2=" + passedProgID + "&arg3=" + progName + "&arg4=" + progDuration + "&arg5=" + progNotes;
                }

                // Only make database changes if creating a new program or existing program data has been changed
                if (bolCreateMode || bolProgDataChanged) {
                    //Create new database connection
                    ServerConnection serverConnection = new ServerConnection();
                    //Setup response value
                    serverConnection.delegate = asyncResponse;
                    //Send data to server
                    serverConnection.execute(data, passedMode); //passedMode to determine the type of query performed
                }
*/
            }
        });

        btnPlan = findViewById(R.id.progCreateDayPlanBtn);
        btnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                progName = tvProgName.getText().toString();
                progDuration = Integer.parseInt(tvProgDuration.getText().toString());
                progNotes = tvProgNotes.getText().toString();

                // ********************* check valid screen values first

                if (!bolCreateMode && (progName.equals(passedProgName) && (progDuration == passedProgDuration) && progNotes.equals(passedProgNotes))) {
                    // if update mode and none of the screen data has changed then just open the day planner screen

                    Intent i = new Intent(context, ProgramDayPlanner.class);
                    i.putExtra("PROGID",progID);
                    i.putExtra("PROGNAME",progName);
                    i.putExtra("DURATION",progDuration);
                    startActivity(i);
                } else {
                    if (bolCreateMode) {
                        SaveData("PLAN");
                    } else {
                        progID = passedProgID;
                        // If the duration has been changed then any existing event data for days greater than the duration will be deleted
                        // so warn the user
                        new AlertDialog.Builder(context)
                            .setTitle("Update Program?")
                            .setMessage("Are you sure you wish to update this program with the chosen data ?\n WARNING! If the duration has been reduced then any existing event data for days greater than the duration will be deleted!")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    SaveData("PLAN");
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
/*                progName = tvProgName.getText().toString();
                progDuration = Integer.parseInt(tvProgDuration.getText().toString());
                progNotes = tvProgNotes.getText().toString();

                if (bolCreateMode) {
                    // Create program
                    data = "programs.php?arg1=ip&arg2=" + progName + "&arg3=" + progDuration + "&arg4=" + progNotes;
                } else {
                    // Update program if it has changed
                    data = "programs.php?arg1=up&arg2=" + passedProgID + "&arg3=" + progName + "&arg4=" + progDuration + "&arg5=" + progNotes;
                }

                if (progName.equals(passedProgName) && (progDuration == passedProgDuration) && progNotes.equals(passedProgNotes)) {
                    bolProgDataChanged = false;
                } else {
                    bolProgDataChanged = true;
                }

                if (bolProgDataChanged) {
                    // User has changed the program data so get user to confirm they wish to update
                    // If the duration has been changed then any existing event data for days greater than the duration will be deleted
                    new AlertDialog.Builder(this)
                        .setTitle("Update Program?")
                        .setMessage("Are you sure you wish to update this program with the chosen data ?\n WARNING! If the duration has been reduced then any existing event data for days greater than the duration will be deleted!")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog

                                dialog.dismiss();
                                //Create new database connection and
                                ServerConnection serverConnection = new ServerConnection();
                                //Setup response value
                                serverConnection.delegate = asyncResponse;
                                //Send data to server
                                serverConnection.execute(data, passedMode); //passedMode to determine the type of query performed

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
*/
//            }
        });

        btnCancel = findViewById(R.id.progCreateCancelBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Close this screen
                finish();
            }
        });

    }

    //Get the result of async process
    public void processFinish(String result, String destination){

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
                    if (bolCreateMode) {
                        errTitle = "Error Creating Program";
                    } else {
                        errTitle = "Error Updating Program";
                    }

                    new AlertDialog.Builder(this)
                            .setTitle(errTitle)
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;

                    if (jo.getString("status").equals("OK")) {
                        jaData = jo.getJSONArray("data");

                        // Reset screen as edit mode
                        passedMode = "EDIT";
                        passedProgID = progID;
                        passedProgName = progName;
                        passedProgDuration = progDuration;
                        passedProgNotes = progNotes;
                        bolCreateMode = false;

                        if (destination.equals("UPDATEPROG")) {
/*                            // If updating a program then delete any previously created events for any days greater than the duration
                            ************
                            data="programs.php?arg1=dmpe&arg2="+passedProgID+"&arg3="+progDuration;

                            //Create new database connection
                            ServerConnection serverConnection=new ServerConnection();
                            //Setup response value
                            serverConnection.delegate=asyncResponse;
                            //Send data to server
                            serverConnection.execute(data,"DELETEEVENTS");
                        }

 */
                            new AlertDialog.Builder(this)
                                    .setTitle("Update Program")
                                    .setMessage("The program was updated successfully")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", null)
                                    .show();

                        } else if (destination.equals("PLAN")) {
                            // program created/updated successfully do now open the day planner screen

                            // Start the Day Planner activity
                            Intent i = new Intent(context, ProgramDayPlanner.class);
                            i.putExtra("PROGID",progID);
                            i.putExtra("PROGNAME", progName);
                            i.putExtra("DURATION",progDuration);
                            startActivity(i);
                        } else if (destination.equals("CREATEPROG")) {
                            new AlertDialog.Builder(this)
                                .setTitle("Create Program")
                                .setMessage("The program was created successfully")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", null)
                                .show();
                    }
//                        else if (destination.equals("DELETEEVENTS")) {
//                        new AlertDialog.Builder(this)
//                                .setTitle("Update Program")
//                                .setMessage("The program was successfully updated")
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .setPositiveButton("OK", null)
//                                .show();
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void SaveData (String ID) {

        //progName = tvProgName.getText().toString();
        //progDuration = Integer.parseInt(tvProgDuration.getText().toString());
        //progNotes = tvProgNotes.getText().toString();

        if (bolCreateMode) {
            // Create program
            data = "programs.php?arg1=ip&arg2=" + progName + "&arg3=" + progDuration + "&arg4=" + progNotes;
        } else {
            // Update program. Also remove events for days greater than the duration
            data = "programs.php?arg1=upae&arg2=" + progID + "&arg3=" + progName + "&arg4=" + progDuration + "&arg5=" + progNotes;

            //if (!progName.equals(passedProgName) || (progDuration != passedProgDuration) || !progNotes.equals(passedProgNotes)) {
                //bolProgDataChanged = true;
                //bolUpdateConfirmed = false; // set default flag to prevent database update

                //if (bolProgDataChanged) {
                // User has changed the program data so get user to confirm they wish to update
                // If the duration has been changed then any existing event data for days greater than the duration will be deleted
/*                new AlertDialog.Builder(this)
                        .setTitle("Update Program?")
                        .setMessage("Are you sure you wish to update this program with the chosen data ?\n WARNING! If the duration has been reduced then any existing event data for days greater than the duration will be deleted!")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog

                                dialog.dismiss();
                                //Create new database connection
                                ServerConnection serverConnection = new ServerConnection();
                                //Setup response value
                                serverConnection.delegate = asyncResponse;
                                //Send data to server
                                serverConnection.execute(data, ID);

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
 */
        }

//      if (bolCreateMode || bolUpdateConfirmed) {
            //Create new database connection
            ServerConnection serverConnection = new ServerConnection();
            //Setup response value
            serverConnection.delegate = asyncResponse;
            //Send data to server
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            serverConnection.execute(data, ID);
        }
        //}

    }

}