package com.example.pt_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CONTACTS = "contacts";



    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_NUMBER + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(Contact contact) {

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the contact already exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS + " WHERE " +
                COLUMN_NUMBER + " = ?", new String[]{contact.getNumber()});

        if (cursor.moveToFirst()) {
            // Contact already exists, you can choose to update it or ignore it
            // For now, I'll just log a message and close the cursor
            Log.d("DatabaseHelper", "Contact already exists with number: " + contact.getNumber());
        } else {
            // Contact doesn't exist, insert it
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, contact.getName());
            values.put(COLUMN_NUMBER, contact.getNumber());
            db.insert(TABLE_CONTACTS, null, values);
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();
    }

    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CONTACTS, null, null, null, null, null, null);
    }





}