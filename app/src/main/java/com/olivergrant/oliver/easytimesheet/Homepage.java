package com.olivergrant.oliver.easytimesheet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class Homepage extends AppCompatActivity {

    static final int REQUEST_CAMERA = 1001;
    static final int REQUEST_WRITE_STORAGE = 1002;
    static final int REQUEST_READ_STORAGE = 1003;

    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    String TAG = "HomepageTAG";
    Boolean activityOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        surfaceView = findViewById(R.id.cameraPreview);

        //Check to make sure the app has permission to use the camera. If false, request camera permission.
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            startScanning();
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(this, "Camera permission is needed to scan QR codes and take photos of employees when needed.", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        //Check to make sure app has permission to storage so QR codes can be generated and saved. Might not need external storage.
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Storage permission is needed to save created QR Codes.", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Storage permission is needed to save created QR Codes.", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        }

        //After permissions have been checked, start controller
        DataController.StartController(Environment.getExternalStorageDirectory());

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA || requestCode == REQUEST_WRITE_STORAGE || requestCode == REQUEST_READ_STORAGE) {
            if(requestCode == REQUEST_CAMERA){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanning();
                } else {
                    Toast.makeText(this, "Camera permission needs to be granted for application to function.", Toast.LENGTH_LONG).show();
                }
            }

            if(requestCode == REQUEST_WRITE_STORAGE){
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Storage permission needs to be granted to create new QR Codes for employees.", Toast.LENGTH_LONG).show();
                }
            }

            if(requestCode == REQUEST_READ_STORAGE){
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Storage permission needs to be granted to create new QR Codes for employees.", Toast.LENGTH_LONG).show();
                }
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        }
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
                        if (!activityOpen) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Homepage.this, "Code: " + qrCodes.valueAt(0).displayValue, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Homepage.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    String code = qrCodes.valueAt(0).displayValue;
                    Employee emp = DataController.FindEmployeeByCode(code);
                    //Make sure employee was found, if so check their last clocktype and then add new one accordingly.
                    if(emp != null){
                        if(emp.getAdmin()){
                            if(!activityOpen){
                                activityOpen = true;
                                startActivity(new Intent(Homepage.this, activityAdminControls.class));
                            }
                            return;
                        }
                        if(emp.CurrentClockTypeAsEnum() == ClockType.ClockIn){
                            emp.addClockTime(ClockType.ClockOut);
                            DataController.UpdateEmployeeClockTimes(emp);
                        }
                        else{
                            emp.addClockTime(ClockType.ClockIn);
                            DataController.UpdateEmployeeClockTimes(emp);
                        }
                        Log.d(TAG, "Employee has been found");
                    }else{
                        Log.d(TAG, "Employee not found");
                    }
                }
            }
        });
    }
}

