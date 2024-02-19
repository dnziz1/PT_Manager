package com.example.pt_app;

public class ProgramEventWorkoutTypeModel {

    int workoutTypeID;
    String name;

    public ProgramEventWorkoutTypeModel(int workoutTypeID, String name) {
        this.workoutTypeID = workoutTypeID;
        this.name = name;
    }
    public int getWorkoutTypeID() {
        return workoutTypeID;
    }
    public void setWorkoutTypeID(int workoutTypeID) {
        this.workoutTypeID = workoutTypeID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
