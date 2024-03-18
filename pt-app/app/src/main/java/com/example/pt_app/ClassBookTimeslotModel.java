package com.example.pt_app;

import java.util.Date;

public class ClassBookTimeslotModel {
    private int timeslotID;
    private int scheduleID;
    private Date startTime;

    public ClassBookTimeslotModel(int timeslotID, int scheduleID, Date startTime) {
        this.timeslotID = timeslotID;
        this.scheduleID = scheduleID;
        this.startTime = startTime;
    }

    public int getTimeslotID() {
        return timeslotID;
    }

    public int  getScheduleID() {
        return scheduleID;
    }

    public Date getStartTime() {
        return startTime;
    }

}
