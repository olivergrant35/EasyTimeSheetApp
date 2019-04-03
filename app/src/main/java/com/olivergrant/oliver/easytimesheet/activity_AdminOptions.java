package com.olivergrant.oliver.easytimesheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class activity_AdminOptions extends AppCompatActivity {

    Switch switchTakePhotos;
    EditText editTextStartOfMonthDay;
    Button buttonSaveOptions;
    Map<String, String> optionsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__admin_options);

        switchTakePhotos = findViewById(R.id.switchTakePhotos);
        editTextStartOfMonthDay = findViewById(R.id.editTextMonthStartDay);
        buttonSaveOptions = findViewById(R.id.buttonSaveOptions);

        switchTakePhotos.setChecked(DataController.getTakePhotos());
        editTextStartOfMonthDay.setText(DataController.getStartOfMonthDay().toString());

        buttonSaveOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add each option to optionsMap with its checked value, then pass that to dataController to save options.
                optionsMap.put("takePhotos", String.valueOf(switchTakePhotos.isChecked()));
                optionsMap.put("startOfMonthDay", editTextStartOfMonthDay.getText().toString());
                DataController.SaveOptions(optionsMap);
                Toast.makeText(activity_AdminOptions.this, "Options Saved.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
