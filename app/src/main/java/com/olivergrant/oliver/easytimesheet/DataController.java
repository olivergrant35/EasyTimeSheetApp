package com.olivergrant.oliver.easytimesheet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

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

public class DataController {

    private static FirebaseDatabase database;
    private static DatabaseReference employeesRef;
    private static StorageReference qrStorageRef;
    private static File filesDir;
    private static String folderPath;
    private static String TAG = "DatabaseControllerError";
    private static Boolean takePhotos = false;

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

    public static String NewEmployeeCode(){
        int c = 1000;
        for (Employee emp: employeeList) {
            if(Integer.parseInt(emp.getEmployeeCode()) >= c){
                c = Integer.parseInt(emp.getEmployeeCode());
            }
        }
        String newCode = Integer.toString(c+1);
        GenerateQRCode(newCode);
        return newCode;
    }

    //Check if required files exist. If not, create them.
    public static void SetUpRequiredFolders(){
        File f = new File(folderPath);
        if(!f.exists()){
            if(!f.mkdirs())
                Log.d(TAG, "Failed to create directories");
        }
    }

    //Save the image to database with the name being the employee code.
    public static void SaveImageToDatabase(String imageName, String code){
        final Uri file = Uri.fromFile(new File(folderPath + "/" + imageName + ".jpg"));
        final File f = new File(folderPath + "/" + imageName + ".jpg");
        StorageReference codeRef = qrStorageRef.child(code);

        //TODO: Add authentication to access the database.
        codeRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        f.delete();
                        Log.d("UploadError", "Uploaded");
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

    public static void GenerateQRCode(String code){
        QRGEncoder qrencoder = new QRGEncoder(code, null, QRGContents.Type.TEXT, 150);
        String imageName = "QRCode" + code;
        try {
            Bitmap b = qrencoder.encodeAsBitmap();
            boolean save;
            save = QRGSaver.save(folderPath + "/", imageName, b, QRGContents.ImageType.IMAGE_JPEG);
            SaveImageToDatabase(imageName, code);
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
