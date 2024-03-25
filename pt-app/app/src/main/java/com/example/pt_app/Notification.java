package com.example.pt_app;

// Model class
public class Notification {
    private int notifID;
    private String title;
    private String details;


    public Notification(int notifID, String title, String description) {
        this.notifID = notifID;
        this.title = title;
        this.details = description;
    }

    public int getNotifID() { return notifID;}
    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }
}
