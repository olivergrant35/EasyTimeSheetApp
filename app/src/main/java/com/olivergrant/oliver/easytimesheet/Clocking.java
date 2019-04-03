package com.olivergrant.oliver.easytimesheet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Clocking {

    private String Date;
    private ClockType ClockingType;

    //Default constructor for firebase.
    public Clocking(){

    }

    public Clocking(String date, ClockType clockType){
        this.Date = date;
        this.ClockingType = clockType;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public ClockType getClockingType() {
        return ClockingType;
    }

    public void setClockingType(ClockType clockingType) {
        ClockingType = clockingType;
    }
}
