package com.example.pt_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Button to get back to homepage and notification
        ImageView home = findViewById(R.id.home);
        ImageView notification = findViewById(R.id.notification);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, HomePage.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        // Retrieve username from intent extras
        Intent intent = getIntent();
        String username = "";
        if (intent != null && intent.hasExtra("username")) {
            username = intent.getStringExtra("username");
        }

        // Get references to TextViews
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView membershipTypeTextView = findViewById(R.id.membershipTypeTextView);
        TextView paymentDetailsTextView = findViewById(R.id.paymentDetailsTextView);

        // Placeholder data, get data from db
        String membershipType = "Standard";
        String paymentDetails = "£45/month";

        // Set data to TextViews
        usernameTextView.setText("Username: admin" + username);
        membershipTypeTextView.setText("Membership Type: " + membershipType);
        paymentDetailsTextView.setText("Payment Details: " + paymentDetails);
    }
    public void setPreferences(String result) {
        //Get just JSON lines of result
        String[] lines = result.split("\n");
        StringBuilder jsonResult = new StringBuilder();

        // Append lines starting from the fourth line
        for (int i = 3; i < lines.length; i++) {
            jsonResult.append(lines[i]).append("\n");
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResult.toString());

            //Get data from JSON
            String userId = jsonObject.getString("userId");
            String username = jsonObject.getString("username");
            String accountType = jsonObject.getString("accountType");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
