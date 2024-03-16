package com.example.pt_app;

public class ClassScheduleClassModel {

    int classID;
    String name;
    int duration;
    int maxOccupancy;
    String notes;


    public ClassScheduleClassModel(int classID, String name, int duration, int maxOccupancy, String notes) {
        this.classID = classID;
        this.name = name;
        this.duration = duration;
        this.maxOccupancy = maxOccupancy;
        this.notes = notes;
    }
    public int getClassID() {
        return classID;
    }
    public void setClassID(int classID) {
        this.classID = classID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getMaxOccupancy() {
        return maxOccupancy;
    }
    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

}
