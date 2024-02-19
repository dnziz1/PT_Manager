package com.example.pt_app;

public class ProgramListLVModel {
    private int progID;
    private String name;
    private int duration;
    private String notes;
    private int trainerID;

    public ProgramListLVModel(int progID, String name, int duration, String notes, int trainerID) {
        this.progID = progID;
        this.name = name;
        this.duration = duration;
        this.notes = notes;
        this.trainerID = trainerID;
    }

    public int getProgID() {
        return progID;
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
