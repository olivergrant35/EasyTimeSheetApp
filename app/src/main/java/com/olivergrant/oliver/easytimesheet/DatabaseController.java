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

    private FirebaseDatabase database;
    private DatabaseReference employeesRef;
    private String TAG = "DatabaseControllerError";

    private List<Employee> employeeList;
    private Employee testEmp;

    public DatabaseController() {
        this.database = FirebaseDatabase.getInstance();
        this.employeesRef = database.getReference("Employees");
        employeeList = new ArrayList<>();
        PopulateEmployeeList();
    }

    private void PopulateEmployeeList(){
        employeesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList.clear();
                testEmp = dataSnapshot.getValue(Employee.class);
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

    public Employee FindEmployeeByCode(String code){
        for(int i=0; i < employeeList.size(); i++){
            if(employeeList.get(i).getEmployeeCode().equals(code)){
                return employeeList.get(i);
            }
        }
        return null;
    }

    public void WriteNewEmployee(Employee emp){
        employeesRef.child(emp.getEmployeeCode()).setValue(emp);
    }

    public String NewEmployeeCode(){
        //TODO: Need to check the last employee code, increment by 1 and return that.
        return "0004";
    }

    public void SetEmployeeAdmin(Employee emp, boolean isAdmin){
        emp.setAdmin(isAdmin);
    }
}
