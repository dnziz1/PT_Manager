package com.example.pt_app;

import static android.widget.AdapterView.*;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    Button btnUpdateSearch;
    ListView lvPrograms;
    ArrayList<ProgramListLVModel> arrPrograms;
    ProgramListLVAdapter lvProgramsAdapter;

    Spinner spTrainers;
    ArrayList<ProgramListTrainerModel> arrTrainers;
    ProgramListTrainerAdapter spTrainersAdapter ;
    TextView tvNameSearch, tvMinDays, tvMaxDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_list);
        asyncResponse = this;
        context = this;
        //context = this;
        tvNameSearch = findViewById(R.id.progListNameSearch);
        tvMinDays = findViewById(R.id.progListMinDays);
        tvMaxDays = findViewById(R.id.progListMaxDays);


        btnUpdateSearch = findViewById(R.id.progListSearchBtn);
        btnUpdateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //populateListUpdate(); // Test code to be replaced

                UpdateListViewData();
                //lvAdapter.notifyDataSetChanged();
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
                // TODO Auto-generated method stub
                int pos = spTrainers.getSelectedItemPosition();

                String trainerID = ((TextView)view.findViewById(R.id.rProgListTrainerID)).getText().toString();
                String trainerName = ((TextView)view.findViewById(R.id.rProgListTrainerName)).getText().toString();

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

                int programID = Integer.parseInt(((TextView)view.findViewById(R.id.rProgListProgID)).getText().toString());
                String name = ((TextView)view.findViewById(R.id.rProgListName)).getText().toString();
               int duration = Integer.parseInt(((TextView)view.findViewById(R.id.rProgListDuration)).getText().toString());
               String notes = ((TextView)view.findViewById(R.id.rProgListNotes)).getText().toString();

                Toast.makeText(getApplicationContext(),
                        "ProgramID : " + programID +"\n"
                                +"Name : " + name +"\n"
                                +"Duration : " + duration +"\n", Toast.LENGTH_SHORT).show();

                // TO DO ***** OPEN EDIT Program Screen for the selected program
               Intent i = new Intent(context, ProgramCreateEdit.class);
               i.putExtra("MODE","EDIT");
               i.putExtra("PROGID",programID);
               i.putExtra("PROGNAME",name);
               i.putExtra("DURATION",duration);
               i.putExtra("NOTES",notes);
               startActivity(i);
           }
        });

        // TEST DATA TO BE REPLACED BY DB DATA
        //populateList();

        //lvAdapter.notifyDataSetChanged();

        // Get program data from the database based on the selection criteria
        UpdateListViewData();

/*        //data = "programs.php?arg1=gpbf&arg2=" + tvNameSearch.getText() + "&arg3=" + tvMinDays.getText() + "&arg4=" + tvMaxDays.getText() + "&arg5=" + spTrainerID.getSelectedItemPosition();
        data = "programs.php?arg1=gpbf&arg2=" + tvNameSearch.getText() + "&arg3=" + tvMinDays.getText() + "&arg4=" + tvMaxDays.getText() + "&arg5=";

        //Create new database connection
        serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"listPrograms");
*/
    }

    // Refresh activity when coming back to it to show any new programs
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    private void populateList() {

        ProgramListLVModel item1, item2, item3, item4, item5;

        item1 = new ProgramListLVModel(1, "30 day test plan", 30);
        arrPrograms.add(item1);

        item2 = new ProgramListLVModel(2, "40 day test plan", 40);
        arrPrograms.add(item2);

        item3 = new ProgramListLVModel(3, "50 day test plan", 50);
        arrPrograms.add(item3);

        item4 = new ProgramListLVModel(4, "60 day test plan", 60);
        arrPrograms.add(item4);

        item5 = new ProgramListLVModel(5, "60 day test plan", 60);
        arrPrograms.add(item5);
    }
    private void populateListUpdate() {

        arrPrograms.clear();
        ProgramListLVModel item1, item2, item3, item4, item5;

        item1 = new ProgramListLVModel(1, "30 day Abs workout", 30);
        arrPrograms.add(item1);

        item2 = new ProgramListLVModel(2, "40 day Hips and thighs plan", 40);
        arrPrograms.add(item2);

        item3 = new ProgramListLVModel(3, "50 day Cardiac Workout Plan", 50);
        arrPrograms.add(item3);

        item4 = new ProgramListLVModel(4, "60 day weight loss program", 60);
        arrPrograms.add(item4);

        item5 = new ProgramListLVModel(5, "60 day Full body workout", 60);
        arrPrograms.add(item5);
    }

    //Get the result of async process
    public void processFinish(String result,String destination){

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
                    int testtid = Integer.parseInt(jo.getString("tId"));
                    String testName = jo.getString("displayName");
                    ProgramListTrainerModel trainer = new ProgramListTrainerModel(Integer.parseInt(jo.getString("tId")), jo.getString("displayName"));
                    //trainer.setTrainerID(jo.getInt("trainerID"));
                    //trainer.setName(jo.getString("name"));
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
                    ProgramListLVModel program = new ProgramListLVModel(Integer.parseInt(jo.getString("programID")), jo.getString("name"), Integer.parseInt(jo.getString("duration")));
                    //program.setProgramID(jo.getInt("programID"));
                    //program.setName(jo.getString("name"));
                    //program.setDuration(jo.getInt("duration"));
                    arrPrograms.add(program);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        lvProgramsAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner

/*        // *********TO DO Code to read no of days from programs table
                // Program ID can be passed from Program Select or from Program Create activities
                // Use no of days  instead of N

                // READ JSON row
                // Get eventday from row
                // If eventday = day loop i then place on screen otherwise continue i loop
                int dataLength = ja.length();
                JSONObject jo = null;


                TextView tvProgID = findViewById(R.id.progListID);
                TextView tvProgName = findViewById(R.id.progListName);
                TextView tvProgDuration = findViewById(R.id.progListDuration);
                //TextView tvProgNotes = findViewById(R.id.progListNotes);
                TextView tvProgTrainerID = findViewById(R.id.progListTrainerID);
                String progID = tvProgID.getText().toString();
                String progName = tvProgName.getText().toString();
                String progDuration = tvProgDuration.getText().toString();
                String progNotes = tvProgNotes.getText().toString();
                String trainerID = tvProgTrainerID.getText().toString();

                for (int i=0; i<dataLength; i++) {

                    try {
                        JSONObject joData = ja.getJSONObject(i);
                        JSONArray data = joData.getJSONArray("data");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    LinearLayout LLH = new LinearLayout(this);
                    LLH.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    LLH.setLayoutParams(params);

                    for (j = 0; j < data.length(); j++) {
                        // create new textviews for each program row
                        TextView tvRowProgID = new TextView(this);
                        TextView tvRowProgName = new TextView(this);
                        TextView tvRowProgDuration = new TextView(this);
                        TextView tvRowProgNotes = new TextView(this);
                        TextView tvRowProgTrainerID = new TextView(this);
                        //rowTextView.setLayoutParams(params);

                        //Linear layout horizontal
                        // set some properties of rowTextView or something
                        tvRowProgID.setText(progID);
                        tvRowProgID.setTag(progID);
                        tvRowProgID.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int selectedProgID = (int) v.getTag();

                                startActivity(new Intent(getApplicationContext(), ProgramCreate.class));
                            }
                        });

                        tvRowProgName.setText(progName);
                        tvRowProgName.setTag(progID);

                        tvRowProgName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int selectedProgID = (int) v.getTag();

                                startActivity(new Intent(getApplicationContext(), ProgramCreate.class));
                            }
                        });

                        tvRowProgDuration.setText(progDuration);
                        tvRowProgDuration.setTag(progID);

                        tvRowProgDuration.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int selectedProgID = (int) v.getTag();

                                startActivity(new Intent(getApplicationContext(), ProgramCreate.class));
                            }
                        });

                        LLH.addView(tvRowProgID);
                        LLH.addView(tvRowProgName);
                        LLH.addView(tvRowProgDuration);
                        // add the textview to the linearlayout
                        LinearLayout layout = (LinearLayout) findViewById(R.id.programListLayout);
                        layout.addView(LLH);
                    }
                }

*/
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
}