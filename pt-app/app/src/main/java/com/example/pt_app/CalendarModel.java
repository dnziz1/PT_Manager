package com.example.pt_app;

public class CalendarModel {
    private int id;
    private String title;
    private String startDate;
    private String endDate;

    public CalendarModel(int id, String title, String startDate, String endDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter methods
    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
