package com.olivergrant.oliver.easytimesheet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

@SuppressWarnings("deprecation")
public class Homepage extends AppCompatActivity {

    Camera cam;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayoutCamera);
        //TODO: Get Camera to open and display correctly in the box.
        //TODO: Camera get instance as i enabled it in phone permissions. Need to make it so it requests permission. 
        cam = getCameraInstance();


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

//    private boolean safeCameraOpen(int id) {
//        boolean qOpened = false;
//
//        try {
//            releaseCameraAndPreview();
//            camera = Camera.open(id);
//            qOpened = (camera != null);
//        } catch (Exception e) {
//            Log.e(getString(R.string.app_name), "failed to open Camera");
//            e.printStackTrace();
//        }
//
//        return qOpened;
//    }

    //Determining which camera is the front facing one and getting it's object.
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            Log.e("CAMERA ERROR", e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    /*Check to see if the device has a camera. Might not need this method as application would
        be on hardware given to client so would always have a camera.
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}
