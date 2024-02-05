package com.example.pt_app;

public class StoredUser {
    //ID not currently fully implemented, but kept with placeholder values.

    int id;
    String name;

    public StoredUser(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
