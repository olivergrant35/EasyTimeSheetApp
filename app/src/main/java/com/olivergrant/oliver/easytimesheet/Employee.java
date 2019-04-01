package com.olivergrant.oliver.easytimesheet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Employee {

    private String FirstName;
    private String Surname;
    private String EmployeeCode;
    private String Fullname;
    private String Email;
    private Boolean IsAdmin;
    private ClockType currentClockType;
    private Map<String, ClockType> ClockTimes = new HashMap<>();

    public Employee(){

    }

    public Employee(String firstName, String surname, String email) {
        FirstName = firstName.trim();
        Surname = surname.trim();
        Email = email.trim();
        EmployeeCode = DataController.NewEmployeeCode();
        Fullname = firstName + " " + surname + " ";
        IsAdmin = false;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public String getFullname() {
        return Fullname;
    }

    public Map<String, ClockType> getClockTimes() {
        return ClockTimes;
    }

    public void addClockTime(ClockType clockType) {
        Date date = Calendar.getInstance().getTime();
        String d = ConvertDateToString(date);
        ClockTimes.put(d, clockType);
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getSurname() {
        return Surname;
    }

    public Boolean getAdmin() {
        return IsAdmin;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public void setEmployeeCode(String employeeCode) {
        EmployeeCode = employeeCode;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public ClockType getCurrentClockType() {
        return currentClockType;
    }

    public void setCurrentClockType(ClockType currentClockType) {
        this.currentClockType = currentClockType;
    }

    public void setClockTimes(Map<String, ClockType> clockTimes) {
        ClockTimes = clockTimes;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setAdmin(Boolean admin) {
        IsAdmin = admin;
    }

    private String ConvertDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String d = sdf.format(date);
        return d;
    }
}
