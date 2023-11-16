package com.dnk.databaseexample1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText event_input, trainer_input, time_input;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        event_input = findViewById(R.id.event_input);
        trainer_input = findViewById(R.id.trainer_input);
        time_input = findViewById(R.id.time_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseExampleHelp exDB = new DatabaseExampleHelp(AddActivity.this);
                exDB.addEvent(event_input.getText().toString().trim(),
                        trainer_input.getText().toString().trim(),
                        Integer.valueOf(time_input.getText().toString().trim()));
            }
        });
    }
}