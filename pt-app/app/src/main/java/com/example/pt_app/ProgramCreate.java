package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ProgramCreate extends AppCompatActivity implements AsyncResponse {
    Button btnDayPlanner, btnCancel;
    EditText programName, programDuration, programNotes;
    String trainerID;
    //Context context;
    AsyncResponse asyncResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_create);
        asyncResponse = this;
        //context = this;

        programName = findViewById(R.id.progCreateName);
        programDuration = findViewById(R.id.progCreateDuration);
        programNotes = findViewById(R.id.progCreateNotes);
        trainerID = "999";
        // TO FIX *********** trainerID = findViewById(R.id.trainerUsernameInput);

        // Find out if creating or editing a program and set the screen accordingly
        Intent intent = getIntent();
        String mode = intent.getStringExtra("MODE");
        String progIDToEdit = intent.getStringExtra("PROGID");
        String progNameToEdit = intent.getStringExtra("PROGNAME");
        String progDurationToEdit = intent.getStringExtra("DURATION");
        String progNotesToEdit = intent.getStringExtra("NOTES");

        if (mode.equals("EDIT")) {
            // COPY DATA FROM LIST SCREEN AND POPULATE ACTIVITY VIEWS
            //programID.setText(progIDToEdit);
            programName.setText(progNameToEdit);
            programDuration.setText(progDurationToEdit);
            programNotes.setText(progNotesToEdit);
        }


        btnDayPlanner = findViewById(R.id.progCreateDayPlanBtn);
        btnDayPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String progName = programName.getText().toString();
                String progDuration = programDuration.getText().toString();

                if (progName.isEmpty()) {
                    programName.setError("Program name is required");
                    programName.requestFocus();
                    return;
                }
                if (progDuration.isEmpty()) {
                    programDuration.setError("Program length is required");
                    programDuration.requestFocus();
                    return;
                }

                int iprogDays = Integer.parseInt(progDuration);

                if (iprogDays == 0 || iprogDays > 365) {
                    programDuration.setError("Program length must be 1 to 365 days");
                    programDuration.requestFocus();
                    return;
                }

                String progNotes = programNotes.getText().toString();
                // TO FIX *** final String progTrainerID = trainerID.getText().toString();
                String progTrainerID = trainerID;

                //HTML encode http request data to handle special characters
                try {
                    progName = URLEncoder.encode(progName, "UTF-8");
                    progDuration = URLEncoder.encode(progDuration, "UTF-8");
                    progNotes = URLEncoder.encode(progNotes, "UTF-8");
                    trainerID = URLEncoder.encode(trainerID, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                String data = "";
                if (mode.equals("EDIT")) {
                    // UPDATE EXISTING PROGRAM
                    data = "programs.php?arg1=up&arg2=" + progIDToEdit + "&arg3=" + progName + "&arg4=" + progDuration + "&arg5=" + progNotes;
                } else {
                    // CREATE Program
                    data = "programs.php?arg1=ip&arg2=" + progName + "&arg3=" + progDuration + "&arg4=" + progNotes + "&arg5=" + trainerID;
                }

                //Create new database connection
                ServerConnection serverConnection = new ServerConnection();
                //Setup response value
                serverConnection.delegate = asyncResponse;
                //Send data to server
                serverConnection.execute(data);
            }
        });

        btnCancel = findViewById(R.id.progCreateCancelBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //Get the result of async process
    public void processFinish(String result, String destination){

        startActivity(new Intent(getApplicationContext(), ProgramDayPlanner.class));

        // CONVERT RESULT STRING TO JSON ARRAY
        JSONArray ja = null;
        try {
            ja = new JSONArray(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        TextView tvProgName = findViewById(R.id.progCreateName);
        TextView tvProgLength = findViewById(R.id.progCreateDuration);
        TextView tvProgNotes = findViewById(R.id.progCreateNotes);
        //TextView tvTrainerID = findViewById(R.id.trainerID);
        int dataLength = ja.length();
        JSONObject jo = null;
        long programID = 0;
        String progName = "";
        String progLength = "";
        String progNotes = "";

        // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
        if (dataLength > 0) {
            try {
                jo = ja.getJSONObject(0);
                if (jo.getString("status").equals("Error")) {
                    Log.d("Error",jo.getString("msg"));

                    new AlertDialog.Builder(this)
                            .setTitle("Error Creating Program")
                            .setMessage(jo.getString("msg"))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject joData = ja.getJSONObject(i);
                        JSONArray data = joData.getJSONArray("data");



                        //id = row.getInt("id");
                        //name = row.getString("name");
                    }
                    JSONObject allData = ja.getJSONObject(0);
                    JSONArray data = allData.getJSONArray("data");
                    JSONObject joData = data.getJSONObject(0);
                    // RETRIEVE JSON OBJECT'S FIELDS
//                  progName = joData.getString("name");
//                  progDuration = joData.getInt("duration");
//                  progNotes = joData.getString("notes");
//                  tvProgName.setText(progName);
//                  tvProgLength.setText(progDuration);
//                  tvProgNotes.setText(progNotes);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
