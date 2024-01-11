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

import java.net.URLEncoder;
import java.security.MessageDigest;

public class TrainerLogin extends AppCompatActivity implements AsyncResponse {
    //Initialise EditText variables
    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_login);

        //Find EditTexts
        usernameInput = findViewById(R.id.trainerUsernameInput);
        passwordInput = findViewById(R.id.trainerPasswordInput);
    }

    public void openClientLogin (View view){
        Intent intent = new Intent (this, ClientLogin.class);
        startActivity(intent);
    }

    public void logIn (View view){
        //Get input values for username and password
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

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
        String data = "trainerlogin&user=" + username + "&pass=" + password;

        //Create new database connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = this;
        //Send data to server
        serverConnection.execute(data);
    }

    //Get the result of async process
    public void processFinish(String result, String destination){
        //Check if login is successful
        if (result.contains("Login successful")){
            //Change activity
            Intent intent = new Intent (this, Calendar.class);
            startActivity(intent);
        //Reset the password input if incorrect
        } else {;
            EditText passwordInput = findViewById(R.id.trainerPasswordInput);
            passwordInput.setText("");
        }
    }
}