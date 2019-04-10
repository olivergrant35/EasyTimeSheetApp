package com.olivergrant.oliver.easytimesheet;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class DataController {

    private static FirebaseDatabase database;
    private static DatabaseReference employeesRef;
    private static DatabaseReference optionsRef;
    private static StorageReference qrStorageRef;
    private static File filesDir;
    private static String folderPath;
    private static String TAG = "DatabaseControllerError";
    private static Boolean takePhotos = false;
    private static Integer startOfMonthDay;
    private static ArrayList<Employee> employeeList;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    //Want the controller to be static so cannot have constructor. Method gets called when Homepage loads.
    public static void StartController(File files) {
        database = FirebaseDatabase.getInstance();
        employeesRef = database.getReference("Employees");
        optionsRef = database.getReference("Options");
        qrStorageRef = FirebaseStorage.getInstance().getReference("QRCodes");
        filesDir = files;
        folderPath = filesDir + "/EasyTimesheetImages";
        SetUpRequiredFolders();
        employeeList = new ArrayList<>();

        //Event listeners for the employees and options saved on database. Updates if change is made.
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
        optionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot optionSnapshot : dataSnapshot.getChildren()){
                    if(optionSnapshot.getKey().equals("takePhotos"))
                        takePhotos = Boolean.parseBoolean((String)optionSnapshot.getValue());
                    else if(optionSnapshot.getKey().equals("startOfMonthDay"))
                        startOfMonthDay = Integer.parseInt((String)optionSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Returns the Employee object that has the passed EmployeeCode.
    public static Employee FindEmployeeByCode(String code){
        for(int i=0; i < employeeList.size(); i++){
            if(employeeList.get(i).getEmployeeCode().equals(code)){
                return employeeList.get(i);
            }
        }
        return null;
    }

    //Writes a new employee to the database.
    public static void WriteNewEmployee(Employee emp){
        String key = employeesRef.push().getKey();
        emp.setDBKey(key);
        employeesRef.child(key).setValue(emp);
    }

    //Updates an employee on the database when they add a new clock time.
    public static void UpdateEmployeeClockTimes(Employee emp){
        String key = employeesRef.push().getKey();
        employeesRef.child(emp.getDBKey()).child("clockTimes").setValue(emp.getClockTimes());
        employeesRef.child(emp.getDBKey()).child("currentClockType").setValue(emp.getCurrentClockType());
    }

    //Generates a new and unique employee code.
    public static String NewEmployeeCode(){
        Random rand = new Random();
        int num = rand.nextInt(9999);
        num += 1000;
        while (CheckIfCodeExists(Integer.toString(num))){
            num += 1;
        }
        GenerateQRCode(Integer.toString(num));
        return Integer.toString(num);
    }

    //Checks if an employee code already exists.
    private static boolean CheckIfCodeExists(String code){
        boolean result = false;
        if (employeeList != null) {
            for(Employee emp : employeeList){
                if(emp.getEmployeeCode().equals(code)){
                    result = true;
                }
            }
        }else{
            return false;
        }
        return result;
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

    //Get image from database for passed Employee
    //TODO: Decide what way I want the user to be able to see the employee photos.
    public static void GetImagesFromDatabase(Employee emp){

    }

    //Generates a new QR code for an employee then calls method to save it to database.
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

    //Saves the options to the database.
    public static void SaveOptions(Map<String, String> options){
        for(Map.Entry<String, String> option : options.entrySet()){
            optionsRef.child(option.getKey()).setValue(option.getValue());
        }
    }

    //Returns the hours worked for given employee in the current month.
    public static String CalculateMonthsHours(Employee emp) throws ParseException {
        Date nowDate = new Date();
        int hours = 0;
        int mins = 0;
        long difference = 0;
        String startDateAsString = CalculateStartingDate();
        String endDateAsString = CalculateEndingDate(startDateAsString);
        Date startDateAsDate = sdf.parse(startDateAsString);
        Date endDateAsDate = sdf.parse(endDateAsString);
        Calendar.getInstance().setTime(nowDate);
        Date todaysDate = Calendar.getInstance().getTime();

        //Map<DateOfDay, hoursWorked>
        Boolean isClockIn = false;
        //Split each clocking into its day.
        //TODO: Test this with changing the date in options.
        for (int i=0; i<emp.getClockTimes().size(); i+=2){
            Date date = sdf.parse(GetClockingDate(emp.getClockTimes().get(i).getDate()));
            //Check if date is inbetween the start and end date.
            if(date.compareTo(startDateAsDate) >= 0 && date.compareTo(endDateAsDate) <= 0){
                //Format the clocking dates into the time format.
                Date date1 = timeFormat.parse(GetDatesTime(emp.getClockTimes().get(i).getDate()));
                Date date2 = timeFormat.parse(GetDatesTime(emp.getClockTimes().get(i+1).getDate()));
                //Calculate difference, then work out the hours and minuets from the difference which is in milliseconds.
                difference = date2.getTime() - date1.getTime();
                hours += (int)(difference / (1000 * 60 * 60));
                mins += (int)(difference/(1000*60)) % 60;
            }
        }
        return Integer.toString(hours) + " Hours " + Integer.toString(mins) + " Mins";
    }

    //Calculates the starting date of the current month for payments.
    private static String CalculateStartingDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if(calendar.get(Calendar.DAY_OF_MONTH) < startOfMonthDay){
            calendar.add(Calendar.MONTH, -1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, startOfMonthDay);
        return sdf.format(calendar.getTime());
    }

    //Calculates the ending date of the current month for payments.
    private static String CalculateEndingDate(String startDate){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(GetDatesDay(startDate)) - 1);
        return sdf.format(calendar.getTime());
    }

    //Converts a Date object to a string in the given format.
    public static String ConvertDateToString(Date date){
        SimpleDateFormat sdft = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String d = sdft.format(date);
        return d;
    }

    //Getters and setters.
    private static String GetDatesMonth(String date){
        return date.substring(3, 5);
    }

    private static String GetDatesDay(String date){
        return date.substring(0, 2);
    }

    private static String GetDatesYear(String date){
        return date.substring(6,10);
    }

    private static String GetClockingDate(String date){
        return date.substring(0, 10);
    }

    private static String GetDatesTime(String date){
        return date.substring(11);
    }

    public static ArrayList<Employee> getEmployeeList() {
        return employeeList;
    }

    public static void SetEmployeeAdmin(Employee emp, boolean isAdmin){
        emp.setAdmin(isAdmin);
    }

    public static Boolean getTakePhotos() {
        return takePhotos;
    }

    public static Integer getStartOfMonthDay() {
        return startOfMonthDay;
    }
}
