package com.example.pt_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();
    private EditText editTextMessage;
    private ImageView buttonSend;
    private TextView textViewDisplay;
    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    private static final String URL = "messages.php"; // Change the URL accordingly

    // RecyclerView components
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);
        textViewDisplay = findViewById(R.id.text_view_display);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recycler_view);
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToDatabase();
            }
        });

        ImageView home = findViewById(R.id.home);
        ImageView notification = findViewById(R.id.notification);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendMessageToDatabase() {
        final String messageText = editTextMessage.getText().toString().trim();
        final String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Update TextView with the message
        textViewDisplay.setText("Message sent: " + messageText);

        // Add the new message to the list of messages with timestamp
        messageList.add(new ChatMessage("sender", messageText, timeStamp));

        // Update the RecyclerView adapter
        messageAdapter.notifyDataSetChanged();

        // Scroll the RecyclerView to the bottom to show the latest message
        recyclerView.scrollToPosition(messageList.size() - 1);

        // Use a Handler to delay adding the automated message
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Add an automated response message after the delay
                String automatedMessage = "This is an automated response. You will receive a reply shortly";
                messageList.add(new ChatMessage("receiver", automatedMessage, timeStamp));

                // Update the RecyclerView adapter after adding the automated message
                messageAdapter.notifyDataSetChanged();

                // Scroll the RecyclerView to the bottom to show the latest message
                recyclerView.scrollToPosition(messageList.size() - 1);

                // Send the message to the database
                sendRequestToDatabase(messageText, timeStamp, automatedMessage);
            }
        }, 5000); // Adjust the delay time (in milliseconds) as needed
    }

    private void sendRequestToDatabase(final String messageText, final String timeStamp, final String automatedMessage) {
        // Send the message to the database
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the server
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            if (status.equals("success")) {
                                // Message sent successfully
                                Toast.makeText(ChatActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Error sending message
                                Toast.makeText(ChatActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChatActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error sending message: " + error.getMessage());
                        Toast.makeText(ChatActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "send");
                params.put("message", messageText);
                params.put("timestamp", timeStamp);
                // Add automated message to the parameters
                params.put("automated_message", automatedMessage);
                return params;
            }
        };

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
