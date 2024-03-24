package com.example.pt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ClientRegister extends AppCompatActivity implements AsyncResponse {

    EditText firstNameInput;
    EditText lastNameInput;
    EditText usernameInput;
    EditText emailInput;
    EditText phoneInput;
    EditText passwordInput;
    EditText confirmPasswordInput;

    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);

        //Find EditTexts
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        errorText = (TextView)findViewById(R.id.errorText);
    }

    public void register (View view) {
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String username = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if(firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            errorText.setText("Please fill all required fields");
        }
        else if(!password.equals(confirmPassword)){
            errorText.setText("Passwords do not match");
        }
        else {
            errorText.setText("");
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
                firstName = URLEncoder.encode(username, "UTF-8");
                lastName = URLEncoder.encode(username, "UTF-8");
                username = URLEncoder.encode(username, "UTF-8");
                password = URLEncoder.encode(generatedPassword, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            //Create new backend connection
            ServerConnection serverConnection = new ServerConnection();
            //Setup response value
            serverConnection.delegate = this;

            //Form parameters into a string
            String data = "CreateAccount.php?fname="+firstName+"&lname="+lastName+"&user="+username+"&pass="+password;
            //If email is entered, add it to the string
            if (!email.isEmpty()){
                data = data + "&email="+email;
            }
            //If phone number is entered, add it to the string
            if (!phone.isEmpty()){
                data = data + "&phone="+phone;
            }

            serverConnection.execute(data,"");
        }
    }

    //Get the result of async process
    public void processFinish(String result, String destination){
        if(result.contains("New account created successfully")){
            //Change activity
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
        else if(result.contains("Username already in use")){
            errorText.setText("Username already in use");
        }
    }
}