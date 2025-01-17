package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
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
    Button btnDelete,btnSave,btnCancel,btnPlan;
    boolean bolCreateMode;
    String progName,progNotes,data,accountType;
    int progID,progDuration;

    int userID;
    TextView tvProgName,tvProgDuration,tvProgNotes;

    int passedProgID,passedProgDuration,passedTrainerID;
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

        // Store passed in variables
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID",0);
        accountType = intent.getStringExtra("accountType");
        passedMode = intent.getStringExtra("MODE");
        passedProgID = intent.getIntExtra("PROGID",0);
        passedProgName = intent.getStringExtra("PROGNAME");
        passedProgDuration = intent.getIntExtra("DURATION",0);
        passedProgNotes = intent.getStringExtra("NOTES");
        passedTrainerID = intent.getIntExtra("TRAINERID",0);
        bolCreateMode = true; // set default mode as creating a new program
        btnDelete = findViewById(R.id.progCreateDeleteBtn);
        btnSave = findViewById(R.id.progCreateSaveBtn);

        // Find out if creating or editing a program and set the screen accordingly
        if (passedMode.equals("EDIT")) {
            bolCreateMode = false;
            // COPY DATA FROM LIST SCREEN AND POPULATE ACTIVITY VIEWS
            //tvprogramID.setText(progIDToEdit);
            tvProgName.setText(passedProgName);
            tvProgDuration.setText(String.valueOf(passedProgDuration));
            tvProgNotes.setText(passedProgNotes);

            // If the trainer is not the one that created the program then set activity as view only
            if (!(passedTrainerID == userID && accountType.equals("TRAINER"))) {
                //tvProgName.setEnabled(false);
                tvProgName.setFocusable(false);
                tvProgName.setActivated(false);
                tvProgName.setInputType(InputType.TYPE_NULL);
                //tvProgDuration.setEnabled(false);
                tvProgDuration.setFocusable(false);
                tvProgDuration.setActivated(false);
                tvProgDuration.setInputType(InputType.TYPE_NULL);
                //tvProgNotes.setEnabled(false);
                tvProgNotes.setFocusable(false);
                tvProgNotes.setActivated(false);
                tvProgNotes.setInputType(InputType.TYPE_NULL);
                btnSave.setEnabled(false);
                btnSave.setFocusable(false);
                btnSave.setActivated(false);
                btnSave.setInputType(InputType.TYPE_NULL);
                btnDelete.setEnabled(false);
                btnDelete.setFocusable(false);
                btnDelete.setActivated(false);
                btnDelete.setInputType(InputType.TYPE_NULL);
            }
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ask user to confirm they wish to delete the program
                //
                new AlertDialog.Builder(context)
                        .setTitle("Delete Program?")
                        .setMessage("Are you sure you wish to delete this program ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                SaveData("DELETEPROG");
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first check screen values are valid
                if (!IsValidData()) {
                    return;
                }

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
            }
        });

        btnPlan = findViewById(R.id.progCreateDayPlanBtn);
        btnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // first screen values are valid
                if (!IsValidData()) {
                    return;
                }

                if (!bolCreateMode && (progName.equals(passedProgName) && (progDuration == passedProgDuration) && progNotes.equals(passedProgNotes))) {
                    // if update mode and none of the screen data has changed then just open the day planner screen

                    Intent i = new Intent(context, ProgramDayPlanner.class);
                    i.putExtra("MODE",passedMode);
                    i.putExtra("userID",userID);
                    i.putExtra("accountType",accountType);
                    i.putExtra("PROGID",passedProgID);
                    i.putExtra("PROGNAME",progName);
                    i.putExtra("DURATION",progDuration);
                    i.putExtra("NOTES",progNotes);
                    i.putExtra("TRAINERID",passedTrainerID);
                    startActivity(i);
                    finish();
                } else {
                    if (bolCreateMode) {
                        SaveData("PLAN");
                    } else {
                        //progID = passedProgID;
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
        });

        if (bolCreateMode) {
            btnDelete.setEnabled(false);
            btnDelete.setFocusable(false);
            btnDelete.setActivated(false);
            btnPlan.setEnabled(false);
            btnPlan.setFocusable(false);
            btnPlan.setActivated(false);
        }

        btnCancel = findViewById(R.id.progCreateCancelBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close activity and return to the program list
                Intent i = new Intent(context, ProgramList.class);
                i.putExtra("userID",userID);
                i.putExtra("accountType",accountType);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the day planner
        Intent i = new Intent(context, ProgramList.class);
        i.putExtra("userID",userID);
        i.putExtra("accountType",accountType);
        startActivity(i);
        finish();
    }

    //Get the result of async process
    public void processFinish(String result, String destination){

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

                    new AlertDialog.Builder(context)
                            .setTitle(errTitle)
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;
                    JSONObject joData = null;

                    if (jo.getString("status").equals("OK")) {
                        // Reset screen as edit mode
                        if (bolCreateMode) {
                            // retrieve newly created programID from DB
                            passedProgID = Integer.parseInt(jo.getString("programID"));
                            progID = passedProgID;
                            passedTrainerID = userID;
                            passedMode = "EDIT";
                            bolCreateMode = false;
                        }

                        passedProgName = progName;
                        passedProgDuration = progDuration;
                        passedProgNotes = progNotes;

                        if (destination.equals("DELETEPROG")) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Delete Program")
                                    .setMessage("The program was deleted successfully")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //close activity and return to the program list activity
                                            Intent i = new Intent(context, ProgramList.class);
                                            i.putExtra("userID",userID);
                                            i.putExtra("accountType",accountType);
                                            startActivity(i);
                                            finish();
                                        }
                                    })

                                    .show();
                        } else if (destination.equals("UPDATEPROG")) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Update Program")
                                    .setMessage("The program was updated successfully")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("OK", null)
                                    .show();
                        } else if (destination.equals("PLAN")) {
                            // program created/updated successfully do now open the day planner activity
                            Intent i = new Intent(context, ProgramDayPlanner.class);
                            i.putExtra("MODE",passedMode);
                            i.putExtra("userID",userID);
                            i.putExtra("accountType",accountType);
                            i.putExtra("PROGID",passedProgID);
                            i.putExtra("PROGNAME",progName);
                            i.putExtra("DURATION",progDuration);
                            i.putExtra("NOTES",progNotes);
                            i.putExtra("TRAINERID",passedTrainerID);
                            startActivity(i);
                            finish();
                        } else if (destination.equals("CREATEPROG")) {
                            new AlertDialog.Builder(context)
                                .setTitle("Create Program")
                                .setMessage("The program was created successfully")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // restart activity in edit mode
                                        Intent i = getIntent();
                                        i.putExtra("MODE",passedMode);
                                        i.putExtra("userID",userID);
                                        i.putExtra("accountType",accountType);
                                        i.putExtra("PROGID",passedProgID);
                                        i.putExtra("PROGNAME",progName);
                                        i.putExtra("DURATION",progDuration);
                                        i.putExtra("NOTES",progNotes);
                                        i.putExtra("TRAINERID",passedTrainerID);
                                        finish();
                                        startActivity(i);
                                    }
                                })
                                .show();
                        }
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void SaveData (String ID) {

        progName = tvProgName.getText().toString();
        progDuration = Integer.parseInt(tvProgDuration.getText().toString());
        progNotes = tvProgNotes.getText().toString();

        if (ID.equals("DELETEPROG")) {
            // Delete program which will also perform a cascading delete of events
            data = "programs.php?arg1=dp&arg2=" + passedProgID;
        } else {
            if (bolCreateMode) {
                // Create program
                data = "programs.php?arg1=ip&arg2=" + progName + "&arg3=" + progDuration + "&arg4=" + progNotes + "&arg5=" + userID;
            } else {
                // Update program. Also remove events for days greater than the duration
                data = "programs.php?arg1=upae&arg2=" + passedProgID + "&arg3=" + progName + "&arg4=" + progDuration + "&arg5=" + progNotes;
            }
        }

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data, ID);
    }

    public boolean IsValidData () {
        // Check screen data is valid and show error message if not
        String errTitle = "", errMsg = "";
        boolean bolValidData = true;

        progName = tvProgName.getText().toString();

        String sProgDuration = tvProgDuration.getText().toString();
        progNotes = tvProgNotes.getText().toString();


        if (bolCreateMode) {
            errTitle = "Create Program";
        } else {
            errTitle = "Update Program";
        }

        if (progName.isEmpty()) {
            errMsg = "Program name is required";
            tvProgName.requestFocus();
            bolValidData = false;
        } else if (sProgDuration.equals("")) {
            errMsg = "Program length is required";
            tvProgDuration.requestFocus();
            bolValidData = false;
        } else {
            progDuration = Integer.parseInt(tvProgDuration.getText().toString());

            if (progDuration == 0 || progDuration > 365) {
                errMsg = "Program length must be 1 to 365 days";
                tvProgDuration.requestFocus();
                bolValidData = false;
            }
        }

        if (bolValidData) {
            return true;
        } else {
            new AlertDialog.Builder(context)
                    .setTitle(errTitle)
                    .setMessage(errMsg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();

            return false;
        }
    }
}