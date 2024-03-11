package com.example.pt_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Button to get back to homepage
        ImageView home = findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Get references to TextViews
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView membershipTypeTextView = findViewById(R.id.membershipTypeTextView);
        TextView paymentDetailsTextView = findViewById(R.id.paymentDetailsTextView);

        // Placeholder data, get data from db
        String username = "Doc Brown";
        String membershipType = "Standard";
        String paymentDetails = "£45/month";

        // Set data to TextViews
        usernameTextView.setText("Username: " + username);
        membershipTypeTextView.setText("Membership Type: " + membershipType);
        paymentDetailsTextView.setText("Payment Details: " + paymentDetails);
    }
}
