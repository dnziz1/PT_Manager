package com.example.pt_app;

public class ProgramListModel {
    private int progID;
    private String name;
    private int duration;

    public ProgramListModel(int progID, String name, int duration) {
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

}
