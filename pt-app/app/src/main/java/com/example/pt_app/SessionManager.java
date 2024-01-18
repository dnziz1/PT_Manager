package com.example.pt_app;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(StoredUser user){
        //save session whenever user is logged in
        int id = user.getId();

        editor.putInt(SESSION_KEY,id).apply();
    }

    public int getSession(){
        //return saved session
        return sharedPreferences.getInt(SESSION_KEY,-1);
    }

    public void removeSession(){
        editor.putInt(SESSION_KEY,-1).apply();
    }
}
