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

    //Account details
    private static final String SHARED_PREFS = "sessionCache";
    private static final String KEY_ACCOUNT_ID = "userId";
    private static final String KEY_ACCOUNT_USER = "username";
    private static final String KEY_ACCOUNT_TYPE = "accountType";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        //Find EditTexts
        usernameInput = findViewById(R.id.clientUsernameInput);
        passwordInput = findViewById(R.id.clientPasswordInput);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Form parameters into a string
        data = "login.php?checkSession=True";

        //Create new backend connection
        ServerConnection serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = this;
        //Send data to server
        serverConnection.execute(data,"");
    }

    public void openTrainerLogin (View view){
        Intent intent = new Intent (this, TrainerLogin.class);
        startActivity(intent);
    }

    public void logIn (View view){
        //Get input values for username and password
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();

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
            data = "login.php?accountType=clientlogin&user=" + username + "&pass=" + password;

            //Create new backend connection
            ServerConnection serverConnection = new ServerConnection();
            //Setup response value
            serverConnection.delegate = this;
            //Send data to server
            serverConnection.execute(data,"");
        }
    }

    //Get the result of async process
    public void processFinish(String result, String destination){
        //If trying to check session data, ONLY check session data
        if ("login.php?checkSession=True".equals(data)) {
            //Check for existing session data
            if (result != null && !result.contains("No active session")) {
                //Store certain session data as preferences
                setPreferences(result);

                // Change activity
                Intent intent = new Intent(this, Calendar.class);
                startActivity(intent);
            }
        }

        //Otherwise, ONLY check login
        else {
            //Check if login is successful
            if (result != null && result.contains("Login successful")) {
                //Store certain session data as preferences
                setPreferences(result);
                Log.d("PreferencesDebug",sharedPreferences.getString(KEY_ACCOUNT_ID,null)+sharedPreferences.getString(KEY_ACCOUNT_USER,null)+sharedPreferences.getString(KEY_ACCOUNT_TYPE,null));

                //Change activity
                Intent intent = new Intent(this, Calendar.class);
                startActivity(intent);

            //Reset the password input if incorrect
            } else {
                EditText passwordInput = findViewById(R.id.clientPasswordInput);
                passwordInput.setText("");
            }
        }
    }

    public void setPreferences(String result){
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

            //Store current user in sharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_ACCOUNT_ID, userId);
            editor.putString(KEY_ACCOUNT_USER, username);
            editor.putString(KEY_ACCOUNT_TYPE, accountType);
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}