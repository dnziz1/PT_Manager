package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    String userType;
    ArrayList<ClassesTrainerModel> arrTrainers;
    ListView lvClasses;
    Spinner spTrainers;
    ClassesTrainerAdapter trainerAdapter;
    ArrayList<ClassesLVModel> arrClasses;
    ClassesLVAdapter lvClassesAdapter;
    Button btnBookings, btnSchedules, btnUpdateSearch;
    TextView tvNameSearch,tvMinDuration,tvMaxDuration;
    AsyncResponse asyncResponse;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        asyncResponse = this;
        context = this;

        // TO DO: GET USERID AND ACCOUNT TYPE FROM SHARED PREFERENCES
        userID = 99999;
        userType = "TRAINER";

        // If user is a client then disable Schedules button
        // If user is a trainer then disable Bookings button
        btnBookings = findViewById(R.id.classesBookingsBtn);
        btnSchedules = findViewById(R.id.classesSchedulesBtn);
        tvNameSearch = findViewById(R.id.classesNameSearch);
        tvMinDuration = findViewById(R.id.classesMinDuration);
        tvMaxDuration = findViewById(R.id.classesMaxDuration);

        if (userType.equals("CLIENT")) {
            btnBookings.setEnabled(true);
            btnSchedules.setEnabled(false);
        } else {
            btnBookings.setEnabled(false);
            btnSchedules.setEnabled(true);
        }

        btnUpdateSearch = findViewById(R.id.classesSearchBtn);
        btnUpdateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateListViewData();
            }
        });

/**        btnCreateProgram = findViewById(R.id.classesCreateBtn);
        btnCreateProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Class Create activity
                Intent i = new Intent(context, ClassCreateEdit.class);
                i.putExtra("MODE","CREATE");
                startActivity(i);
            }
        });
*/
        arrTrainers = new ArrayList<ClassesTrainerModel>();
        spTrainers = findViewById(R.id.classesTrainerID);
        trainerAdapter = new ClassesTrainerAdapter(this, arrTrainers);
        spTrainers.setAdapter(trainerAdapter);

        spTrainers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                int pos = spTrainers.getSelectedItemPosition();

                String trainerID = ((TextView)view.findViewById(R.id.rClassesTrainerID)).getText().toString();
                String trainerName = ((TextView)view.findViewById(R.id.rClassesTrainerName)).getText().toString();

                Toast.makeText(getApplicationContext(),
                        "TrainerID : " + trainerID +"\n"
                                +"Name : " + trainerName +"\n", Toast.LENGTH_SHORT).show();

                // TO DO get ArrayList data using pos
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

                Toast.makeText(getApplicationContext(),
                        "ClassID : " + classID +"\n"
                                +"Name : " + name +"\n"
                                +"Duration : " + duration +"\n", Toast.LENGTH_SHORT).show();

                // OPEN EDIT Class create/edit Screen for the selected program
                Intent i = new Intent(context, ClassInfo.class);
                i.putExtra("MODE","EDIT");
                i.putExtra("CLASSID",classID);
                i.putExtra("CLASSNAME",name);
                i.putExtra("DURATION",duration);
                i.putExtra("NOTES",notes);
                i.putExtra("TRAINERID",trainerID);
                startActivity(i);
            }
        });

        // Get class data from the database based on the selection criteria
        UpdateListViewData();
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

                        if (destination.equals("listClasses")) {
                            PopulateClassesView(jaData);
                        } else if (destination.equals("spinTrainer")) {
                            //Populate Spinner
                            PopulateTrainerSpinner(jaData);
                        }
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
                    int testtid = Integer.parseInt(jo.getString("tId"));
                    String testName = jo.getString("displayName");
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
                    ClassesLVModel newClass = new ClassesLVModel(Integer.parseInt(jo.getString("classID")), jo.getString("name"), Integer.parseInt(jo.getString("duration")),jo.getString("notes"), Integer.parseInt(jo.getString("trainerID")));
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
        //close activity and return to the Classes activity
        finish();
    }

}