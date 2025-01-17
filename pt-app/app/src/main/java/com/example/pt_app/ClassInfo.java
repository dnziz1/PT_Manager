package com.example.pt_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClassInfo extends AppCompatActivity {

    Button btnCancel,btnBook;
    TextView tvClassName,tvDuration,tvNotes,tvTrainerName;
    int userID,passedClassID,passedDuration,passedTrainerID;
    String accountType,passedClassName,passedClassNotes,passedTrainerName;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        context = this;

        // Store passed in variables
        // Get userid and account type and set screen accordingly
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID",0);
        accountType = intent.getStringExtra("accountType");

//        // TEST
//        userID = 99999;
//        userType = "CLIENT";

        passedClassID = intent.getIntExtra("CLASSID",0);
        passedClassName = intent.getStringExtra("CLASSNAME");
        passedDuration = intent.getIntExtra("DURATION",0);
        passedClassNotes = intent.getStringExtra("NOTES");
        passedTrainerID = intent.getIntExtra("TRAINERID",0);
        passedTrainerName = intent.getStringExtra("TRAINERNAME");
        tvClassName = findViewById(R.id.classInfoName);
        tvDuration = findViewById(R.id.classInfoDuration);
        tvTrainerName = findViewById(R.id.classInfoTrainerName);
        tvNotes = findViewById(R.id.classInfoNotes);
        btnCancel = findViewById(R.id.classInfoCancelBtn);
        btnBook = findViewById(R.id.classInfoBookBtn);

        if (accountType.equals("CLIENT")) {
            btnBook.setEnabled(true);
        } else {
            btnBook.setEnabled(false);
        }

        // Set activity fields to passed in data and not editable
        tvClassName.setText(passedClassName);
        //tvClassName.setEnabled(false);
        tvClassName.setFocusable(false);
        tvClassName.setActivated(false);
        tvDuration.setText(String.valueOf(passedDuration));
        //tvDuration.setEnabled(false);
        tvDuration.setFocusable(false);
        tvDuration.setActivated(false);
        tvTrainerName.setText(passedTrainerName);
        //tvTrainerName.setEnabled(false);
        tvTrainerName.setFocusable(false);
        tvTrainerName.setActivated(false);
        tvNotes.setText(passedClassNotes);
        //tvNotes.setEnabled(false);
        tvNotes.setFocusable(false);
        tvNotes.setActivated(false);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //close activity and return to the Classes activity
                Intent i = new Intent(context, Classes.class);
                i.putExtra("userID",userID);
                i.putExtra("accountType",accountType);
                startActivity(i);
                finish();
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                BookClass();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //close activity and return to the Classes activity
        Intent i = new Intent(context, Classes.class);
        i.putExtra("userID",userID);
        i.putExtra("accountType",accountType);
        startActivity(i);
        finish();
    }

    public void BookClass() {
        //close activity and display the ClassBooking activity so that the client can book a timeslot
        Intent i = new Intent(context, ClassBook.class);
        i.putExtra("userID",userID);
        i.putExtra("accountType",accountType);
        i.putExtra("CLASSID",passedClassID);
        i.putExtra("CLASSNAME",passedClassName);
        i.putExtra("DURATION",passedDuration);
        i.putExtra("NOTES",passedClassNotes);
        i.putExtra("TRAINERID",passedTrainerID);
        i.putExtra("TRAINERNAME",passedTrainerName);
        startActivity(i);
        finish();
    }

}