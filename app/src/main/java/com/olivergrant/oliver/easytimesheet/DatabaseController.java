package com.olivergrant.oliver.easytimesheet;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import java.io.File;
import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class DatabaseController {

    private static FirebaseDatabase database;
    private static DatabaseReference employeesRef;
    private static StorageReference qrStorageRef;
    private static File filesDir;
    private static String folderPath;
    private static String TAG = "DatabaseControllerError";

    private static ArrayList<Employee> employeeList;

    //Want the controller to be static so cannot have constructor. Method gets called when Homepage loads.
    public static void StartController(File files) {
        database = FirebaseDatabase.getInstance();
        employeesRef = database.getReference("Employees");
        qrStorageRef = FirebaseStorage.getInstance().getReference("QRCodes");
        filesDir = files;
        folderPath = filesDir + "/EasyTimesheetImages";
        SetUpRequiredFolders();
        employeeList = new ArrayList<>();
        employeesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList.clear();
                for(DataSnapshot employeeSnapshot : dataSnapshot.getChildren()){
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

    //TODO: Need to generate a QR code aswell.
    public static String NewEmployeeCode(){
        int c = 1000;
        for (Employee emp: employeeList) {
            if(Integer.parseInt(emp.getEmployeeCode()) >= c){
                c = Integer.parseInt(emp.getEmployeeCode());
            }
        }
        return Integer.toString((c+1));
    }

    //TODO: Check if the temp folder exists, if false then create it.
    public static void SetUpRequiredFolders(){
        File f = new File(folderPath);
        if(!f.exists() || !f.isDirectory()){
            f.mkdirs();
        }
    }

    //TODO: Save image to database.
    public static void SaveImageToDatabase(String imageName, Employee emp){
        Uri file = Uri.fromFile(new File(folderPath + "/" + imageName));
        StorageReference codeRef = qrStorageRef.child(emp.getEmployeeCode());

        codeRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO: Decide how to handle a failed upload.
                        Log.d("UploadError", e.getMessage());
                    }
                });
    }

    public static void GetImagesFromDatabase(Employee emp){

    }

    public void GenerateQRCode(String code){
        QRGEncoder qrencoder = new QRGEncoder(code, null, QRGContents.Type.TEXT, 150);
        try {
            Bitmap b = qrencoder.encodeAsBitmap();
            //QRGSaver.save()
        }catch (WriterException e){
            Log.v("EMPLOYEE ERROR", e.toString());
        }
    }

    public static ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }

    public static void SetEmployeeAdmin(Employee emp, boolean isAdmin){
        emp.setAdmin(isAdmin);
    }
}
