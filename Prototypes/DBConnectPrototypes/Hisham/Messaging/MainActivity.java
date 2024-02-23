package com.soft.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText search;
    ArrayList<Item> itemList;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       search= (EditText) findViewById(R.id.edit_text_search);

       ListView listView = findViewById(R.id.chatListView);
       dbHelper = new DatabaseHelper(MainActivity.this);

       itemList = new ArrayList<>();

       Cursor cursor = dbHelper.getAllContacts();

       if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));
                itemList.add(new Item(R.drawable.image1, name, number));
            }
            while (cursor.moveToNext());
       }

       cursor.close();

       adapter = new ChatAdapter(this, R.layout.list_item, itemList);

       listView.setAdapter(adapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("val","true");
            startActivity(intent);
        }
       });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle FAB click here, for example, open a new activity
                startActivity(new Intent(MainActivity.this, ContactList.class));
            }
        });



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }



    private void filter(String text) {
        if (text.isEmpty()) {
            // If the search text is empty, show the complete list
            itemList.clear();
            Cursor cursor = dbHelper.getAllContacts();
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));
                    itemList.add(new Item(R.drawable.image1, name, number));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            // Filter the list based on the search text
            ArrayList<Item> filteredList = new ArrayList<>();
            for (Item item : itemList) {
                if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            itemList.clear();
            itemList.addAll(filteredList);
        }
        adapter.notifyDataSetChanged();
    }
}
