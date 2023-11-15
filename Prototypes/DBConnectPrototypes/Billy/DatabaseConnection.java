package com.example.comp6000app;

import android.os.AsyncTask;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection extends AsyncTask<Void, Void, String> {
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String YOUR_TABLE = "message";
    private static final String USER = "root";
    private static final String PASS = "test";


    // Process the result set or perform other database operations

    //resultSet.close();
    //statement.close();
    //connection.close();

    private String result;
    private TextView resultTextView;

    public DatabaseConnection(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + YOUR_TABLE);

            // Process the result set or perform other database operations

            result = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            result = "Error: " + e.getMessage();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // Update the UI with the result
        resultTextView.setText(result);
    }
}