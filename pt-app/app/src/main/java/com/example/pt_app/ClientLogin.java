package com.example.pt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ClientLogin extends AppCompatActivity implements AsyncResponse {
    //Initialise EditText variables
    EditText usernameInput;
    EditText passwordInput;

    //Initialise username and password strings
    String username;
    String password;

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        //Find EditTexts
        usernameInput = findViewById(R.id.clientUsernameInput);
        passwordInput = findViewById(R.id.clientPasswordInput);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Create new backend connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = this;
        //Send data to server
        serverConnection.execute("login.php","");
    }

    public void openTrainerLogin (View view){
        Intent intent = new Intent (this, TrainerLogin.class);
        startActivity(intent);
    }

    public void logIn (View view){
        //Get input values for username and password
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();

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

            //Create new backend connection
            ServerConnection serverConnection = new ServerConnection();
            //Setup response value
            serverConnection.delegate = this;
            //Form parameters into a string
            data = "login.php?accountType=clientlogin&user=" + username + "&pass=" + password;
            serverConnection.execute(data,"");
        }

        //Form parameters into a string
        String data = "login.php?accountType=clientlogin&user=" + username + "&pass=" + password;

        //Create new backend connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = this;
        //Send data to server
        serverConnection.execute(data,"");
            //Create new backend connection
            ServerConnection serverConnection = new ServerConnection();
            //Setup response value
            serverConnection.delegate = this;
            //Form parameters into a string
            data = "login.php?accountType=clientlogin&user=" + username + "&pass=" + password;
            serverConnection.execute(data,"");
        }
    }

    //Get the result of async process
    public void processFinish(String result, String destination){
        Log.d("PHP RESULT: ",result);

        //Check if login is successful
        if (result != null && result.contains("Login successful")) {
            //Change activity
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);

        //Reset the password input if incorrect
        } else if (!result.contains("No account type provided")) {
            EditText passwordInput = findViewById(R.id.clientPasswordInput);
            passwordInput.setText("");
        }
    }
}