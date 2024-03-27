package com.example.pt_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements AsyncResponse{

    private TextView textViewUsername;
    private TextView textViewUserId;
    private TextView textViewAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        // Initialize TextView elements
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewUserId = findViewById(R.id.textViewUserId);
        textViewAccountType = findViewById(R.id.textViewAccountType);

        // Retrieve user information from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = preferences.getString("userId", "");
        String username = preferences.getString("username", "");
        String accountType = preferences.getString("accountType", "");

        // Set user information to TextView elements
        textViewUsername.setText("Username: " + username);
        textViewUserId.setText("User ID: " + userId);
        textViewAccountType.setText("Account Type: " + accountType);
    }
    //Get the result of async process
    public void processFinish(String result, String destination){
        Log.d("PHP RESULT: ",result);
        // Check if login is successful
        if (result != null && result.contains("Login successful")) {
            // Extract user ID from the result
            String userId = extractUserId(result);
            // Store the user ID in SharedPreferences
            storeUserId(userId);
            // Change activity
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        } else if (!result.contains("No account type provided")) {
            EditText passwordInput = findViewById(R.id.clientPasswordInput);
            passwordInput.setText("");
        }
    }

    // Method to extract user ID from the PHP result
    private String extractUserId(String result) {
        String userId = ""; // Initialize user ID
        try {
            JSONObject jsonObject = new JSONObject(result);
            userId = jsonObject.getString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userId;
    }

    // Method to store user ID in SharedPreferences
    private void storeUserId(String userId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

}

