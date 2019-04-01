package com.olivergrant.oliver.easytimesheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

public class activity_AdminOptions extends AppCompatActivity {

    Switch switchTakePhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__admin_options);

        switchTakePhotos = findViewById(R.id.switchTakePhotos);

        //TODO:Make it so the options persist in the dataController and so it works.
    }
}
