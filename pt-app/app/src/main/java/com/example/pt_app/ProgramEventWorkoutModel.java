package com.example.pt_app;

public class ProgramEventWorkoutModel {

    int workoutID;
    String muscleGroup;
    String name;
    String level;
    String equipment;

    public ProgramEventWorkoutModel(int workoutID, String muscleGroup, String name, String level, String equipment) {
        this.workoutID = workoutID;
        this.muscleGroup = muscleGroup;
        this.name = name;
        this.level = level;
        this.equipment = equipment;
    }
    public int getWorkoutID() {
        return workoutID;
    }
    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }
    public String getMuscleGroup() {
        return muscleGroup;
    }
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getEquipment() {
        return equipment;
    }
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

}
