package com.example.pt_app;

public class ClassesLVModel {
    private int classID;
    private String name;
    private int duration;
    private String notes;
    private int trainerID;

    public ClassesLVModel(int classID, String name, int duration, String notes, int trainerID) {
        this.classID = classID;
        this.name = name;
        this.duration = duration;
        this.notes = notes;
        this.trainerID = trainerID;
    }

    public int getClassID() {
        return classID;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
    public String getNotes() {
        return notes;
    }
    public int getTrainerID() {
        return trainerID;
    }


}
