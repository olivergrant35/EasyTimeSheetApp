package com.olivergrant.oliver.easytimesheet;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController {

    private static FirebaseDatabase database;
    private static DatabaseReference employeesRef;
    private static String TAG = "DatabaseControllerError";

    private static List<Employee> employeeList;

    //Want the controller to be static so cannot have constructor. Method gets called when Homepage loads.
    public static void StartController() {
        database = FirebaseDatabase.getInstance();
        employeesRef = database.getReference("Employees");
        employeeList = new ArrayList<>();
        employeesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList.clear();
                for(DataSnapshot employeeSnapshot : dataSnapshot.getChildren()){
                    //TODO: employee returns as the object but all properties are null. Fix this.
                    Employee employee = employeeSnapshot.getValue(Employee.class);
                    employeeList.add(employee);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Employee FindEmployeeByCode(String code){
        for(int i=0; i < employeeList.size(); i++){
            if(employeeList.get(i).getEmployeeCode().equals(code)){
                return employeeList.get(i);
            }
        }
        return null;
    }

    public static void WriteNewEmployee(Employee emp){
        String key = employeesRef.push().getKey();
        employeesRef.child(key).setValue(emp);
    }

    public static String NewEmployeeCode(){
        //TODO: Need to check the last employee code, increment by 1 and return that.
        int c = 1000;
        for (Employee emp: employeeList) {
            if(Integer.parseInt(emp.getEmployeeCode()) >= c){
                c = Integer.parseInt(emp.getEmployeeCode());
            }
        }
        return Integer.toString((c+1));
    }

    public static void SetEmployeeAdmin(Employee emp, boolean isAdmin){
        emp.setAdmin(isAdmin);
    }
}
