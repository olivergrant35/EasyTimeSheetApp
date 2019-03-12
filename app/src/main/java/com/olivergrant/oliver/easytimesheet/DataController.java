package com.olivergrant.oliver.easytimesheet;

import java.util.ArrayList;

public class DataController {

    private static ArrayList<Employee> employees = new ArrayList<>();

    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static void CreateEmployee(){
        if(employees.size() == 0)
            employees.add(new Employee("Oliver", "Grant", DataController.NewEmployeeCode()));
    }

    public static String NewEmployeeCode(){
        //TODO: Need to check the last employee code, increment by 1 and return that.
        return "0004";
    }

    public static java.sql.Date ConvertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
}
