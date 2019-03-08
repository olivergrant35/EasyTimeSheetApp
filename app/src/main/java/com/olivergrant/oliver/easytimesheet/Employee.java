package com.olivergrant.oliver.easytimesheet;

import java.util.Date;
import java.util.Map;

public class Employee {

    private String FirstName;
    private String Surname;
    private String EmployeeCode;
    private String Fullname;
    private Map<Date, ClockType> ClockTimes;

    public Employee(String firstName, String surname, String employeeCode) {
        FirstName = firstName;
        Surname = surname;
        EmployeeCode = employeeCode;
        Fullname = firstName + " " + surname + " ";
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public String getFullname() {
        return Fullname;
    }

    public Map<Date, ClockType> getClockTimes() {
        return ClockTimes;
    }

    public void addClockTime(ClockType clockType) {
        Date date = new Date();
        ClockTimes.put(date, clockType);
    }
}
