package com.example.pt_app;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ServerConnection extends AsyncTask<String, Void, String> {
    String result;
    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(String... params) {
        String data = params[0];
        result = "";
        this.delegate = delegate;

        try {
            //Update the URL to include the parameters
            URL url = new URL("http://10.0.2.2:8000/server.php?action=" + data);
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
        delegate.processFinish(result);
    }
}