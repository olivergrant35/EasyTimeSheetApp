package com.olivergrant.oliver.easytimesheet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class Homepage extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    TextView textViewStatus;
    String TAG = "HomepageTAG";
    Boolean activityOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        //TODO: Camera get instance as i enabled it in phone permissions. Need to make it so it requests permission.
        surfaceView = findViewById(R.id.cameraPreview);
        textViewStatus = findViewById(R.id.textViewStatusUpdate);
        DatabaseController.StartController();

        //dbController.WriteNewEmployee(new Employee("Admin", "Admin"));
        //dbController.WriteNewEmployee(new Employee("Oliver", "Grant"));
        startScanning();

        //Getting that buttons
        final Button buttonChangeSignInMethod = findViewById(R.id.buttonChangeSignInMethod);

        //Adding click listeners to the buttons and giving them functionality.
        buttonChangeSignInMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the sign in with code activity
                startActivity(new Intent(Homepage.this, activity_codeScreen.class));
            }
        });
    }

    //Method will start and display the camera and also scan for QR codes.
    private void startScanning(){
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        //TODO: uncomment setFacing to change camera to front cam. Set to rear for testing.
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                //.setFacing(1)
                .setRequestedPreviewSize(300, 300).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                activityOpen = false;
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if(qrCodes.size() !=0){
                    if(qrCodes.valueAt(0).displayValue.length() == 4){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewStatus.setText(qrCodes.valueAt(0).displayValue);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //TODO: Make it display "Invalid login".
                            }
                        });
                    }
                    //TODO: Need to create a database of employees and check the code scan against it. SHOULD BE DONE. TEST.
                    //TODO: Check if they have had a clock in already, if so, clock out. Need to consider multiple clocking in a day. SHOULD BE DONE, TEST MULTIPLE SAME DAY.
                    String code = qrCodes.valueAt(0).displayValue;
                    Employee emp = DatabaseController.FindEmployeeByCode(code);
                    //Make sure employee was found, if so check their last clocktype and then add new one accordingly.
                    if(emp != null){
                        if(emp.getAdmin()){
                            //TODO: Need to make it so it will only open once, atm opens multiple times.
                            if(!activityOpen){
                                activityOpen = true;
                                startActivity(new Intent(Homepage.this, activityAdminControls.class));
                            }
                            return;
                        }
                        if(emp.getCurrentClockType() == ClockType.ClockIn)
                            emp.addClockTime(ClockType.ClockOut);
                        else
                            emp.addClockTime(ClockType.ClockIn);
                        Log.d(TAG, "Employee has been found");
                    }else{
                        Log.d(TAG, "Employee not found");
                    }
                }
            }
        });
    }
}

