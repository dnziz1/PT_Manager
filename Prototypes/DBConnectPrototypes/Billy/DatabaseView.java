package com.example.comp6000app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_view);

        // Find the TextView in your layout
        TextView resultTextView = findViewById(R.id.resultTextView);

        // Execute the DatabaseConnection to connect to MySQL and display the result
        new DatabaseConnection(resultTextView).execute();
    }
}