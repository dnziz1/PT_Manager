package com.example.pt_app;

public class ClassesTrainerModel {

    int trainerID;
    String name;

    public ClassesTrainerModel(int trainerID, String name) {
        this.trainerID = trainerID;
        this.name = name;
    }
    public int getTrainerID() {
        return trainerID;
    }
    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
