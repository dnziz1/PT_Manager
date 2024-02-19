package com.soft.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private ImageView buttonSend;
    private List<ChatMessage> messages;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recycler_view);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);

       String val  = getIntent().getStringExtra("val");

        messages = new ArrayList<>();



        if(val.equals("true")) {
            for (int i = 0; i <= 16; i++) {

                if (i % 2 == 0) {
                    messages.add(new ChatMessage("hi! I am Hisham", false));
                } else {
                    messages.add(new ChatMessage("hello! hello I am Javed", true));
                }
            }
        }

        adapter = new MessageAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    messages.add(new ChatMessage(messageText, true));
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.smoothScrollToPosition(messages.size() - 1);
                    editTextMessage.setText("");
                    // Here you can implement logic to send the message to the other user
                    // and receive a response.
                }
            }
        });
    }
}
