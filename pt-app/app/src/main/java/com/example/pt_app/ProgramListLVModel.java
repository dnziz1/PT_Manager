package com.example.pt_app;

public class ProgramListLVModel {
    private int progID;
    private String name;
    private int duration;
    private String notes;

    public ProgramListLVModel(int progID, String name, int duration) {
        this.progID = progID;
        this.name = name;
        this.duration = duration;
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

}
