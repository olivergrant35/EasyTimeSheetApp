package com.olivergrant.oliver.easytimesheet;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        //cam = Camera.open();

        //Getting that buttons
        final Button buttonChangeSignInMethod = findViewById(R.id.buttonChangeSignInMethod);
        final ImageButton buttonOpenAdminLogin = findViewById(R.id.imageButtonOpenAdminLogin);

        //Adding click listeners to the buttons and giving them functionality.
        buttonChangeSignInMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the sign in with code activity
                startActivity(new Intent(Homepage.this, activity_codeScreen.class));
            }
        });

        //Start the admin login activity
        buttonOpenAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, activity_AdminLogin.class));
            }
        });
    }
}
