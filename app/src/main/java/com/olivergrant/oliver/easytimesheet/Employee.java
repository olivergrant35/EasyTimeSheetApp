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
    private String DBKey;
    private String Email;
    private Boolean IsAdmin;
    private ClockType currentClockType;
    private Map<String, Clocking> ClockTimes = new HashMap<>();

    //Default constructor for firebase database.
    public Employee(){

    }

    public Employee(String firstName, String surname, String email) {
        FirstName = firstName.trim();
        Surname = surname.trim();
        Email = email.trim();
        EmployeeCode = DataController.NewEmployeeCode();
        currentClockType = ClockType.ClockOut;
        Fullname = firstName + " " + surname + " ";
        IsAdmin = false;
    }

    public String getEmployeeCode() {
        return EmployeeCode;
    }

    public String getFullname() {
        return Fullname;
    }

    public Map<String, Clocking> getClockTimes() {
        return ClockTimes;
    }

    public void addClockTime(ClockType clockType) {
        Date date = Calendar.getInstance().getTime();
        String d = ConvertDateToString(date);
        currentClockType = clockType;
        ClockTimes.put(d, new Clocking(d, clockType));
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

    public String getCurrentClockType() {
        return currentClockType.name();
    }

    public void setCurrentClockType(String currentClockTypeString) {
        this.currentClockType = ClockType.valueOf(currentClockTypeString);
    }

    public ClockType CurrentClockTypeAsEnum(){
        return currentClockType;
    }

    public void setClockTimes(Map<String, Clocking> clockTimes) {
        ClockTimes = clockTimes;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDBKey() {
        return DBKey;
    }

    public void setDBKey(String DBKey) {
        this.DBKey = DBKey;
    }

    public void setAdmin(Boolean admin) {
        IsAdmin = admin;
    }

    private String ConvertDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String d = sdf.format(date);
        return d;
    }
}
