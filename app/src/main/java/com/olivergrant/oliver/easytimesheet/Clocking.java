package com.olivergrant.oliver.easytimesheet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Clocking {

    private String Date;
    private ClockType ClockingType;

    public Clocking(Date date, ClockType clockType){
        this.Date = ConvertDateToString(date);
        this.ClockingType = clockType;
    }

    private String ConvertDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String d = sdf.format(date);
        return d;
    }
}
