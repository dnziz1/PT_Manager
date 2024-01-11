package com.example.pt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

public class ProgramEventCreate extends AppCompatActivity implements AsyncResponse {
    AsyncResponse asyncResponse;
    Button cancelBtn, saveBtn;
    // create array of Strings
    // and store name of courses
    String[] workouts = {"Data structures",
            "Interview prep", "Algorithms",
            "DSA with java", "OS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_event_create);
        asyncResponse = this;
        // Take the instance of Spinner and
        // apply OnItemSelectedListener on it which
        // tells which item of spinner is clicked
        Spinner spin = findViewById(R.id.programWorkoutSpinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // make toastof name of course
                // which is selected in spinner
                Toast.makeText(getApplicationContext(),
                                workouts[position],
                                Toast.LENGTH_LONG)
                        .show();

                // TODO Auto-generated method stub
                int pos = spin.getSelectedItemPosition();

                // TO DO get ArrayList data using pos
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad = new ArrayAdapter(
                this,
            android.R.layout.simple_spinner_item,
            workouts);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin.setAdapter(ad);

        saveBtn = findViewById(R.id.programEventSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
            }
        });

        cancelBtn = findViewById(R.id.programEventCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close activity and return to the day planner
                finish();
            }
        });

    }
    public void saveEvent() {

        // ****** TO DO Code that inserts event to the database
        // dialog box "Event saved successfully
        finish();
    }

    //Get the result of async process
    public void processFinish(String result, String destination) {
    }
}