package com.example.pt_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.security.NoSuchAlgorithmException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.security.MessageDigest;

public class TrainerLogin extends AppCompatActivity implements AsyncResponse {
    //Initialise EditText variables
    EditText usernameInput;
    EditText passwordInput;

    //Initialise username and password strings
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_login);

        //Find EditTexts
        usernameInput = findViewById(R.id.trainerUsernameInput);
        passwordInput = findViewById(R.id.trainerPasswordInput);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //SessionManager sessionManager = new SessionManager(TrainerLogin.this);
        //int userID = sessionManager.getSession();

        ////If user is logged in
        //if (userID != -1){
        //    //Change activity
        //    Intent intent = new Intent (this, Calendar.class);
        //    startActivity(intent);
        //}

        //Form parameters into a string
        String data = "login.php?checkSession=True";

        //Create new backend connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = this;
        //Send data to server
        serverConnection.execute(data,"");
    }

    public void openClientLogin (View view){
        Intent intent = new Intent (this, ClientLogin.class);
        startActivity(intent);
    }

    public void logIn (View view){
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();

        //Get input values for username and password
        try {
            //Hash password using MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();

            //HTML encode username and password to handle special characters
            username = URLEncoder.encode(username, "UTF-8");
            password = URLEncoder.encode(generatedPassword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        //Form parameters into a string
        String data = "login.php?accountType=trainerlogin&user=" + username + "&pass=" + password;

        //Create new backend connection (Get rid of when session checks fully work - meaning just one server connection that stays up whilst on login activity)
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = this;
        //Send data to server
        serverConnection.execute(data,"");
    }

    //Get the result of async process
    public void processFinish(String result, String destination){
        //Check for existing session data
        if (result != null && !result.contains("No active session")) {
            //Change activity
            Intent intent = new Intent (this, Menu.class);
            startActivity(intent);
        }
        //Check if login is successful
        else if (result != null && result.contains("Login successful")){
            //Set session
            StoredUser storedUser = new StoredUser(1,username);
            SessionManager sessionManager = new SessionManager(TrainerLogin.this);
            sessionManager.saveSession(storedUser);
            //Change activity
            Intent intent = new Intent (this, Menu.class);
            startActivity(intent);
        //Reset the password input if incorrect
        } else {
            EditText passwordInput = findViewById(R.id.trainerPasswordInput);
            passwordInput.setText("");
        }
    }
}