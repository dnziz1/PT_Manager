package com.example.pt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgramDayPlanner extends AppCompatActivity implements AsyncResponse {

    AsyncResponse asyncResponse;
    //Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_day_planner);
        asyncResponse = this;
        //context = this;

        final int N = 10; // TO FIX **** test total number of textviews to add
        final LinearLayout[] myDayHeaders = new LinearLayout[N]; // create an empty array;
        final TextView[] myTextViews = new TextView[N]; // create an empty array;

        //TextView tvProgName = (TextView) findViewById(R.id.progCreateName);
        //
        // CREATE Program
        //String data = "programs.php?arg1=ip&arg2=" + progName + "&arg3=" + progDuration + "&arg4=" + progNotes + "&arg5=" + trainerID;

        //Create new database connection
        //ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        //serverConnection.delegate = asyncResponse;
        //Send data to server
        //serverConnection.execute(data);

    }

    // Refresh activity when coming back to it to show any new events(workouts/tasks etc)
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    //Get the result of async process
    public void processFinish(String result, String destination){

        // CONVERT RESULT STRING TO JSON ARRAY
        JSONArray ja = null;
        try {
            ja = new JSONArray(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // *********TO DO Code to read no of days from programs table
        // Program ID can be passed from Program Select or from Program Create activities
        // Use no of days  instead of N

        // READ JSON row
        // Get eventday from row
        // If eventday = day loop i then place on screen otherwise continue i loop
        int dataLength = ja.length();
        JSONObject jo = null;
        long programID = 0;
        String email = "";
        String password = "";
        String createTime = "";

        for (int day = 1; day <= 10; day++) {

            LinearLayout LLH = new LinearLayout(this);
            LLH.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            LLH.setLayoutParams(params);


            // create a new textview
            final TextView rowTextView = new TextView(this);
            rowTextView.setLayoutParams(params);

            // ******* PLACE Day and Add event on same line
            // clicking Add event opens pop up window to choose a workout, task or custom text

            //Linear layout horizontal

            // set some properties of rowTextView or something
            rowTextView.setText("Day " + day);

            Button btnAddEvent = new Button(this);
            btnAddEvent.setText("Add Event");

            // add day and Add event button on the same row
            LLH.addView(rowTextView);
            LLH.addView(btnAddEvent);
            // add the textview to the linearlayout
            LinearLayout layout = (LinearLayout) findViewById(R.id.programDayPlanLayout);
            layout.addView(LLH);

            btnAddEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ProgramEventCreate.class));
                    //finish();
                }
            });

            // save a reference to the textview for later
            //myTextViews[i] = rowTextView;

            // ******** TODO Code a loop to read events from the events table


            // ITERATE THROUGH JSONArray AND UPDATE ACTIVITY FIELDS

            //for (int i = 0; i < n; i++) {
            // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
            if ( dataLength > 0) {
                for (int j = 0; j < dataLength; j++) {
                    try {
                        jo = ja.getJSONObject(j);
                        // RETRIEVE EACH JSON OBJECT'S FIELDS

                        programID = jo.getLong("programID");

                        if (programID == day) {
                            email = jo.getString("email");
                            password = jo.getString("password");
                            createTime = jo.getString("create_time");

                            // CREATE DYNAMIC EVENT ROWS WITH EDIT BUTTON
                            // build rowTextView from event and notes

                            LinearLayout LLE = new LinearLayout(this);
                            LLE.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams paramsE = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            paramsE.setMargins(10, 10, 10, 10);
                            LLE.setLayoutParams(paramsE);

                            // create a new textview
                            final TextView rowTextViewE = new TextView(this);
                            rowTextViewE.setLayoutParams(paramsE);

                            // ******* PLACE event and EDIT button on same line
                            // clicking Edit event opens pop up window to amend workout, task or custom text

                            //Linear layout horizontal

                            // set some properties of rowTextView or something
                            rowTextViewE.setText(programID + " : " + email);

                            Button btnEditEvent = new Button(this);
                            btnEditEvent.setText("Edit");

                            // add day and Add event button on the same row
                            LLE.addView(rowTextViewE);
                            LLE.addView(btnEditEvent);
                            // add the textview to the linearlayout
                            LinearLayout layoutE = (LinearLayout) findViewById(R.id.programDayPlanLayout);
                            layoutE.addView(LLE);

                            btnEditEvent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Context mContext = MyApp.getAppContext();
                                    //Intent iEditEvent = new Intent(getApplicationContext(), ProgramEventCreate.class);
                                    //Intent iEditEvent = new Intent(mContext, ProgramEventCreate.class);
                                    //startActivity(iEditEvent);
                                    startActivity(new Intent(getApplicationContext(), ProgramEventCreate.class));

                                    //mContext.startActivity(iEditEvent);
                                    //finish();
                                }
                            });
                        }
                    } catch(JSONException e){
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}