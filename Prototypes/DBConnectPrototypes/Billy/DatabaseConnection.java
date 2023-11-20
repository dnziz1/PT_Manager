package com.example.comp6000app;

import android.os.AsyncTask;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DatabaseConnection extends AsyncTask<String, Void, String> {
    private String result;
    private TextView resultTextView;

    public DatabaseConnection(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    //@Override
    protected String doInBackground(String... params) {
        String query = params[0];
        String result = "";

        try {
            // Encode the query parameter to handle special characters
            String encodedQuery = URLEncoder.encode(query, "UTF-8");

            // Update the URL to include the query parameter
            URL url = new URL("http://10.0.2.2:8000/server.php?query=" + encodedQuery);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // Update the UI with the result
        resultTextView.setText(result);
    }
}
