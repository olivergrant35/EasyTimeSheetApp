package com.olivergrant.oliver.easytimesheet;

public class DataController {
    public static String NewEmployeeCode(){
        //TODO: Need to check the last employee code, increment by 1 and return that.
        return "0004";
    }

    public static java.sql.Date ConvertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
}
