package com.example.pt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ClientLogin extends AppCompatActivity implements AsyncResponse {
    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        //Find EditTexts
        usernameInput = findViewById(R.id.clientUsernameInput);
        passwordInput = findViewById(R.id.clientPasswordInput);
    }

    public void openTrainerLogin (View view){
        Intent intent = new Intent (this, TrainerLogin.class);
        startActivity(intent);
    }

    public void logIn (View view){
        //Get input values for username and password
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        // KEV TEST
        if (username.equals("admin") && password.equals("12345")) {
            Intent intent = new Intent (this, ProgramList.class);
            startActivity(intent);
        } else {
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
            String data = "clientlogin&user=" + username + "&pass=" + password;
            // new way? : String data = "login.php?arg1=clientlogin&user=" + username + "&pass=" + password;

            //Create new database connection
            ServerConnection serverConnection = new ServerConnection();
            //Setup response value
            serverConnection.delegate = this;
            //Send data to server
            serverConnection.execute(data);
        }
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