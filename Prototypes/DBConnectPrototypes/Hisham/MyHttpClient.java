package com.abs.fitnessapp;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyHttpClient {

    private static final String BASE_URL = "https://api.softzonesolution.com/appDb/api.php";



    public MyHttpClient() {
        // Create an OkHttpClient instance with a logging interceptor for debugging
        // You can remove the logging interceptor in production

    }

    public static String executePostRequest(String jsonBody ) {


         OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();


        try {
            // Create a JSON media type
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            // Create a request body with the JSON payload
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);

            // Create a POST request with the URL and request body
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(requestBody)
                    .build();

            // Execute the request and get the response
            Response response = client.newCall(request).execute();

            // Check if the request was successful (HTTP status code 200)
            if (response.isSuccessful()) {
                // Read the response body as a string
                return response.body().string();
            } else {
                // Handle unsuccessful response
                return "Error: " + response.code();
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

//    public static void main(String[] args) {
//        // Create JSON payload with the provided parameters
//        String jsonPayload = "{"
//                + "\"sId\": 1,"
//                + "\"trainerId\": 116,"
//                + "\"clientId\": 767,"
//                + "\"eventDate\": \"2024-01-24\","
//                + "\"startTime\": \"06:30:00\","
//                + "\"endTime\": \"11:00:00\","
//                + "\"title\": \"Training Session 1\","
//                + "\"details\": \"Details of the training session\""
//                + "}";
//
//        // Create an instance of MyHttpClient
//        MyHttpClient httpClient = new MyHttpClient();
//
//        // Execute the POST request with the JSON payload
//        String response = httpClient.executePostRequest(jsonPayload);
//
//        // Print the response
//        System.out.println("Response: " + response);
//    }
}
