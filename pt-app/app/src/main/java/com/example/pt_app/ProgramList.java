package com.example.pt_app;

import static android.widget.AdapterView.*;

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
    //Context context;
    Button btnUpdateSearch;
    ListView lvPrograms;
    ProgramListLVAdapter lvAdapter;
    ArrayList<ProgramListLVModel> arrProgramList;
    ArrayList<ProgramListTrainerModel> arrTrainers;
    ProgramListTrainerAdapter trainerAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_list);
        asyncResponse = this;
        //context = this;

        btnUpdateSearch = findViewById(R.id.progListSearchBtn);
        btnUpdateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateListUpdate(); // Test code to be replaced
                lvAdapter.notifyDataSetChanged();
            }
        });

        arrTrainers = new ArrayList<ProgramListTrainerModel>();
        arrProgramList = new ArrayList<ProgramListLVModel>();
        lvPrograms = (ListView) findViewById(R.id.progListLV);
        lvAdapter = new ProgramListLVAdapter(this, arrProgramList);
        lvPrograms.setAdapter(lvAdapter);

        lvPrograms.setOnItemClickListener(new OnItemClickListener() {
           @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String programID = ((TextView)view.findViewById(R.id.rProgListProgID)).getText().toString();
                String name = ((TextView)view.findViewById(R.id.rProgListName)).getText().toString();
                String duration = ((TextView)view.findViewById(R.id.rProgListDuration)).getText().toString();

                Toast.makeText(getApplicationContext(),
                        "ProgramID : " + programID +"\n"
                                +"Name : " + name +"\n"
                                +"Duration : " + duration +"\n", Toast.LENGTH_SHORT).show();

                // TO DO ***** OPEN EDIT Program Screen for the selected program
           }
        });

        // TEST DATA TO BE REPLACED BY DB DATA
        populateList();

        lvAdapter.notifyDataSetChanged();

        final int N = 10; // TO FIX **** test total number of textviews to add
        final LinearLayout[] myDayHeaders = new LinearLayout[N]; // create an empty array;
        final TextView[] myTextViews = new TextView[N]; // create an empty array;
        TextView tvNameSearch = findViewById(R.id.progListNameSearch);
        TextView tvMinDays = findViewById(R.id.progListMinDays);
        TextView tvMaxDays = findViewById(R.id.progListMaxDays);

        Spinner spTrainerID = findViewById(R.id.progListTrainerID);
        spTrainerID.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                int pos = spTrainerID.getSelectedItemPosition();

                // TO DO get ArrayList data using pos
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*        // Get all trainers from the database to build the trainer spinner
        String data = "programs.php?arg1=gat";

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = asyncResponse;
        //Send data to server
        serverConnection.execute(data,"spinTrainer");


        // Get program data from the database based on the selection criteria
        data = "programs.php?arg1=gpbf&arg2=" + tvNameSearch.getText() + "&arg3=" + tvMinDays.getText() + "&arg4=" + tvMaxDays.getText() + "&arg5=" + spTrainerID.getSelectedItemPosition();

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
        arrProgramList.add(item1);

        item2 = new ProgramListLVModel(2, "40 day test plan", 40);
        arrProgramList.add(item2);

        item3 = new ProgramListLVModel(3, "50 day test plan", 50);
        arrProgramList.add(item3);

        item4 = new ProgramListLVModel(4, "60 day test plan", 60);
        arrProgramList.add(item4);

        item5 = new ProgramListLVModel(5, "60 day test plan", 60);
        arrProgramList.add(item5);
    }
    private void populateListUpdate() {

        arrProgramList.clear();
        ProgramListLVModel item1, item2, item3, item4, item5;

        item1 = new ProgramListLVModel(1, "30 day Abs workout", 30);
        arrProgramList.add(item1);

        item2 = new ProgramListLVModel(2, "40 day Hips and thighs plan", 40);
        arrProgramList.add(item2);

        item3 = new ProgramListLVModel(3, "50 day Cardiac Workout Plan", 50);
        arrProgramList.add(item3);

        item4 = new ProgramListLVModel(4, "60 day weight loss program", 60);
        arrProgramList.add(item4);

        item5 = new ProgramListLVModel(5, "60 day Full body workout", 60);
        arrProgramList.add(item5);
    }

    //Get the result of async process
    public void processFinish(String result,String destination){

        // CONVERT RESULT STRING TO JSON ARRAY
        JSONArray ja = null;
        try {
            ja = new JSONArray(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (ja.length() > 0) {
            try {
                JSONObject jo = ja.getJSONObject(0);

                if (jo.getString("status").equals("Error")) {
                    Log.d("Error", jo.getString("msg"));

                    new AlertDialog.Builder(this)
                            .setTitle("Error Retrieving Programs")
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    JSONArray jaData = jo.getJSONArray("data");
                    if (destination.equals("progListTrainerID")) {
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

        // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
        //if (ja.length() > 0) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    ProgramListTrainerModel trainer = new ProgramListTrainerModel(jo.getInt("trainerID"),jo.getString("name"));
                    //trainer.setTrainerID(jo.getInt("trainerID"));
                    //trainer.setName(jo.getString("name"));
                    arrTrainers.add(trainer);
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
            trainerAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner

        //}
    }

    private void PopulateProgramListView (JSONArray ja) {

        for (int i = 0; i < ja.length(); i++) {
            try {
                JSONObject jo = ja.getJSONObject(i);
                ProgramListLVModel program = new ProgramListLVModel(jo.getInt("programID"),jo.getString("name"),jo.getInt("duration"));
                //program.setProgramID(jo.getInt("programID"));
                //program.setName(jo.getString("name"));
                //program.setDuration(jo.getInt("duration"));
                arrProgramList.add(program);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
        lvAdapter.notifyDataSetChanged(); // this will make the adapter refresh the data in the spinner

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
}