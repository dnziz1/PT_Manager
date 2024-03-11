package com.example.pt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class LogoutTest extends AppCompatActivity implements AsyncResponse {

    ServerConnection serverConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_test);
    }

    public void logOut() {
        //Create new backend connection
        serverConnection = new ServerConnection();
        //Setup response value
        serverConnection.delegate = this;
        //Open up login.php - sets up session data
        serverConnection.execute("login.php","");
    }

    public void processFinish(String result, String destination){
        //Change activity
        Intent intent = new Intent (this, ClientLogin.class);
        startActivity(intent);
    }
}