package com.example.pt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button btnCalendar, btnMessages, btnNotifications, btnPrograms;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        context = this;

        btnCalendar = findViewById(R.id.menuCalendarBtn);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Calendar activity
                Intent i = new Intent(context, Calendar.class);
                startActivity(i);
            }
        });

        btnMessages = findViewById(R.id.menuMessagesBtn);
        btnMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Messages activity
                //Intent i = new Intent(context, Messages.class);
                //startActivity(i);
            }
        });

        btnNotifications = findViewById(R.id.menuNotificationsBtn);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Program Create activity
                Intent i = new Intent(context, NotificationActivity.class);
                startActivity(i);
            }
        });

        btnPrograms = findViewById(R.id.menuProgramsBtn);
        btnPrograms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Program Create activity
                Intent i = new Intent(context, ProgramList.class);
                startActivity(i);
            }
        });
    }
}