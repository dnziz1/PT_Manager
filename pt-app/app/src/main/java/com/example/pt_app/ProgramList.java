package com.example.pt_app;

import static android.widget.AdapterView.*;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProgramList extends AppCompatActivity implements AsyncResponse {
    AsyncResponse asyncResponse;
    Context context;
    Button btnUpdateSearch, btnCreateProgram;
    ListView lvPrograms;
    ArrayList<ProgramListLVModel> arrPrograms;
    ProgramListLVAdapter lvProgramsAdapter;

    Spinner spTrainers;
    ArrayList<ProgramListTrainerModel> arrTrainers;
    ProgramListTrainerAdapter spTrainersAdapter ;
    TextView tvNameSearch, tvMinDays, tvMaxDays;
    int userID;
    String username,accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_list);
        asyncResponse = this;
        context = this;
        tvNameSearch = findViewById(R.id.progListNameSearch);
        tvMinDays = findViewById(R.id.progListMinDays);
        tvMaxDays = findViewById(R.id.progListMaxDays);
        btnUpdateSearch = findViewById(R.id.progListSearchBtn);
        btnCreateProgram = findViewById(R.id.progListCreateBtn);

        // Get userid and account type and set screen accordingly
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID",0);
        accountType = intent.getStringExtra("accountType");

        if (accountType.equals("CLIENT")) {
            btnCreateProgram.setEnabled(false);
            btnCreateProgram.setFocusable(false);
            btnCreateProgram.setActivated(false);
            btnCreateProgram.setInputType(InputType.TYPE_NULL);
        }

        // TEST userID = 99999;
        // TEST accountType = "TRAINER";

        btnUpdateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //populateListUpdate(); // Test code to be replaced

                UpdateListViewData();
                //lvAdapter.notifyDataSetChanged();
            }
        });

        btnCreateProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Program Create activity
                Intent i = new Intent(context, ProgramCreateEdit.class);
                i.putExtra("MODE","CREATE");
                i.putExtra("userID",userID);
                i.putExtra("accountType",accountType);
                startActivity(i);
                finish();
            }
        });

        arrTrainers = new ArrayList<ProgramListTrainerModel>();
        spTrainers = findViewById(R.id.progListTrainerID);
        spTrainersAdapter = new ProgramListTrainerAdapter(this, arrTrainers);
        spTrainers.setAdapter(spTrainersAdapter);

        spTrainers.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get all trainers from the database to build the trainer spinner
        String data = "programs.php?arg1=gat";

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"spinTrainer");

        arrPrograms = new ArrayList<ProgramListLVModel>();
        lvPrograms = (ListView) findViewById(R.id.progListLV);
        lvProgramsAdapter = new ProgramListLVAdapter(this, arrPrograms);
        lvPrograms.setAdapter(lvProgramsAdapter);

        lvPrograms.setOnItemClickListener(new OnItemClickListener() {
           @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int programID = Integer.parseInt(((TextView)view.findViewById(R.id.rProgListLVProgID)).getText().toString());
                String name = ((TextView)view.findViewById(R.id.rProgListLVName)).getText().toString();
               int duration = Integer.parseInt(((TextView)view.findViewById(R.id.rProgListLVDuration)).getText().toString());
               String notes = ((TextView)view.findViewById(R.id.rProgListLVNotes)).getText().toString();
               int trainerID = Integer.parseInt(((TextView)view.findViewById(R.id.rProgListLVTrainerID)).getText().toString());

                // OPEN EDIT Program Screen for the selected program
               Intent i = new Intent(context, ProgramCreateEdit.class);
               i.putExtra("MODE","EDIT");
               i.putExtra("userID",userID);
               i.putExtra("accountType",accountType);
               i.putExtra("PROGID",programID);
               i.putExtra("PROGNAME",name);
               i.putExtra("DURATION",duration);
               i.putExtra("NOTES",notes);
               i.putExtra("TRAINERID",trainerID);
               startActivity(i);
               finish();
           }
        });

        // Get program data from the database based on the selection criteria
        UpdateListViewData();
    }

    //Get the result of async process
    public void processFinish(String result,String destination){

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

                        new AlertDialog.Builder(this)
                                .setTitle("Error Retrieving Programs")
                                .setMessage(jo.getString("msg"))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        JSONArray jaData = null;

                        if (jo.getString("status").equals("OK")) {
                            jaData = jo.getJSONArray("data");
                        }

                        if (destination.equals("spinTrainer")) {
                            PopulateTrainerSpinner(jaData);
                        } else if (destination.equals("listPrograms")) {
                            PopulateProgramListView(jaData);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
//        }
    }

    private void PopulateTrainerSpinner (JSONArray ja) {
        // Populate Trainer spinner with db data

        arrTrainers.clear();
        // add default All option
        arrTrainers.add(new ProgramListTrainerModel(0,"ANY"));
        // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
        if (!(ja == null)) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    //ProgramListTrainerModel trainer = new ProgramListTrainerModel(Integer.parseInt(jo.getString("tId")), jo.getString("displayName"));
                    ProgramListTrainerModel trainer = new ProgramListTrainerModel(Integer.parseInt(jo.getString("trainerID")), jo.getString("displayName"));
                    arrTrainers.add(trainer);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        spTrainersAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner

        //}
    }

    private void PopulateProgramListView (JSONArray ja) {

        arrPrograms.clear();

        if (!(ja == null)) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    ProgramListLVModel program = new ProgramListLVModel(Integer.parseInt(jo.getString("programID")), jo.getString("name"), Integer.parseInt(jo.getString("duration")),jo.getString("notes"), Integer.parseInt(jo.getString("trainerID")));
                    arrPrograms.add(program);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        lvProgramsAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner

    }

    private void UpdateListViewData() {
        // Get a list of programs from the database based on the selection criteria

        // determine whether a specific trainer has been selected or default to all
        int selectedTrainerPos = spTrainers.getSelectedItemPosition();
        String argTrainerID = "";
        if (selectedTrainerPos >=0) {
            ProgramListTrainerModel mSelectedTrainer = arrTrainers.get(selectedTrainerPos);
            argTrainerID = String.valueOf(mSelectedTrainer.getTrainerID());
            if (argTrainerID.equals("0")) argTrainerID = "";
        } else argTrainerID = "";

        // set up PHP data
        String data = "programs.php?arg1=gpbf&arg2=" + tvNameSearch.getText() + "&arg3=" + tvMinDays.getText() + "&arg4=" + tvMaxDays.getText() + "&arg5=" + argTrainerID;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"listPrograms");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}