package com.olivergrant.oliver.easytimesheet;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseController {

    private FirebaseDatabase database;
    private String TAG = "DatabaseControllerError";

    public DatabaseController() {
        this.database = FirebaseDatabase.getInstance();
    }

    public void writeNewEmployee(Employee emp){
        database.getReference().child("employees").child(emp.getEmployeeCode()).setValue(emp);
    }

}
