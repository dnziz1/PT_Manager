package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Classes extends AppCompatActivity implements AsyncResponse {
    int userID;
    String accountType;
    ArrayList<ClassesTrainerModel> arrTrainers;
    ListView lvClasses;
    Spinner spTrainers;
    ClassesTrainerAdapter trainerAdapter;
    ArrayList<ClassesLVModel> arrClasses;
    ClassesLVAdapter lvClassesAdapter;
    Button btnBookings, btnSchedules, btnUpdateSearch,btnCreateClassSchedule;
    TextView tvNameSearch,tvMinDuration,tvMaxDuration;
    AsyncResponse asyncResponse;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        asyncResponse = this;
        context = this;

        // Get userid and account type and set screen accordingly
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID",0);
        accountType = intent.getStringExtra("accountType");

//        // TEST
//        userID = 99999;
//        accountType = "TRAINER";

        // If user is a client then disable Schedules button
        // If user is a trainer then disable Bookings button
        btnBookings = findViewById(R.id.classesBookingsBtn);
        btnSchedules = findViewById(R.id.classesSchedulesBtn);
        btnCreateClassSchedule = findViewById(R.id.classesCreateBtn);

        if (accountType.equals("CLIENT")) {
            btnBookings.setEnabled(true);
            btnSchedules.setEnabled(false);
            btnCreateClassSchedule.setEnabled(false);
        } else {
            btnBookings.setEnabled(false);
            btnSchedules.setEnabled(true);
            btnCreateClassSchedule.setEnabled(true);
        }

        tvNameSearch = findViewById(R.id.classesNameSearch);
        tvMinDuration = findViewById(R.id.classesMinDuration);
        tvMaxDuration = findViewById(R.id.classesMaxDuration);

        btnUpdateSearch = findViewById(R.id.classesSearchBtn);
        btnUpdateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateListViewData();
            }
        });

        btnCreateClassSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Open the Class Create activity
                Intent i = new Intent(context, ClassSchedule.class);
                i.putExtra("userID",userID);
                i.putExtra("accountType",accountType);
                startActivity(i);
                finish();
            }
        });

        btnBookings = findViewById(R.id.classesBookingsBtn);
        btnBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Open the Class Create activity
                Intent i = new Intent(context, ClassBookings.class);
                i.putExtra("userID",userID);
                i.putExtra("accountType",accountType);
                startActivity(i);
                finish();
            }
        });

        btnSchedules = findViewById(R.id.classesSchedulesBtn);
        btnSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Open the Class Create activity
                Intent i = new Intent(context, ClassSchedules.class);
                i.putExtra("userID",userID);
                i.putExtra("accountType",accountType);
                startActivity(i);
                finish();
            }
        });

        arrTrainers = new ArrayList<ClassesTrainerModel>();
        spTrainers = findViewById(R.id.classesTrainerID);
        trainerAdapter = new ClassesTrainerAdapter(this, arrTrainers);
        spTrainers.setAdapter(trainerAdapter);

        spTrainers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get all trainers from the database to build the trainer spinner
        String data = "classes.php?arg1=gat";

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"spinTrainer");


        arrClasses = new ArrayList<ClassesLVModel>();
        lvClasses = (ListView) findViewById(R.id.classesList);
        lvClassesAdapter = new ClassesLVAdapter(this, arrClasses);
        lvClasses.setAdapter(lvClassesAdapter);

        lvClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int classID = Integer.parseInt(((TextView)view.findViewById(R.id.rClassesLVClassID)).getText().toString());
                String name = ((TextView)view.findViewById(R.id.rClassesLVName)).getText().toString();
                int duration = Integer.parseInt(((TextView)view.findViewById(R.id.rClassesLVDuration)).getText().toString());
                String notes = ((TextView)view.findViewById(R.id.rClassesLVNotes)).getText().toString();
                int trainerID = Integer.parseInt(((TextView)view.findViewById(R.id.rClassesLVTrainerID)).getText().toString());
                String trainerName = ((TextView)view.findViewById(R.id.rClassesLVTrainerName)).getText().toString();

                // OPEN EDIT Class create/edit Screen for the selected program
                Intent i = new Intent(context, ClassInfo.class);
                i.putExtra("MODE","EDIT");
                i.putExtra("userID",userID);
                i.putExtra("accountType",accountType);
                i.putExtra("CLASSID",classID);
                i.putExtra("CLASSNAME",name);
                i.putExtra("DURATION",duration);
                i.putExtra("NOTES",notes);
                i.putExtra("TRAINERID",trainerID);
                i.putExtra("TRAINERNAME",trainerName);
                startActivity(i);
                finish();
            }
        });

        // Get class data from the database based on the selection criteria
        UpdateListViewData();
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

                    new AlertDialog.Builder(context)
                            .setTitle("Error Retrieving Classes")
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = null;
                    int classesFound = 0;

                    if (jo.getString("status").equals("OK")) {
                        jaData = jo.getJSONArray("data");
                        classesFound = jaData.length();
                    }

                    if (destination.equals("listClasses")) {
                        PopulateClassesView(jaData);
                    } else if (destination.equals("spinTrainer")) {
                        //Populate Spinner
                        PopulateTrainerSpinner(jaData);
                    }
                }
            }
        } catch (Exception e) {
            new AlertDialog.Builder(context)
                    .setTitle("Classes - Serious Error")
                    .setMessage(e.getMessage())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("OK", null)
                    .show();
        }
//    }

    }

    private void PopulateTrainerSpinner (JSONArray ja) {
        // Populate Trainer spinner with db data

        arrTrainers.clear();
        // add default All option
        arrTrainers.add(new ClassesTrainerModel(0,"ANY"));
        // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
        if (!(ja == null)) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    ClassesTrainerModel trainer = new ClassesTrainerModel(Integer.parseInt(jo.getString("tId")), jo.getString("displayName"));
                    arrTrainers.add(trainer);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        trainerAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner
    }

    private void PopulateClassesView (JSONArray ja) {

        arrClasses.clear();

        if (!(ja == null)) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    ClassesLVModel newClass = new ClassesLVModel(Integer.parseInt(jo.getString("classID")), jo.getString("name"), Integer.parseInt(jo.getString("duration")),jo.getString("notes"), Integer.parseInt(jo.getString("trainerID")),jo.getString("trainer_name"));
                    arrClasses.add(newClass);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        lvClassesAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner

    }

    private void UpdateListViewData() {
        // Get a list of classes from the database based on the selection criteria

        // determine whether a specific trainer has been selected or default to all
        int selectedTrainerPos = spTrainers.getSelectedItemPosition();
        String argTrainerID = "";
        if (selectedTrainerPos >=0) {
            ClassesTrainerModel mSelectedTrainer = arrTrainers.get(selectedTrainerPos);
            argTrainerID = String.valueOf(mSelectedTrainer.getTrainerID());
            if (argTrainerID.equals("0")) argTrainerID = "";
        } else argTrainerID = "";

        // set up PHP data
        String data = "classes.php?arg1=gcbf&arg2=" + tvNameSearch.getText() + "&arg3=" + tvMinDuration.getText() + "&arg4=" + tvMaxDuration.getText() + "&arg5=" + argTrainerID;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"listClasses");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the HomePage activity
        finish();
    }

}