package com.olivergrant.oliver.easytimesheet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class activity_codeScreen extends AppCompatActivity {

    String TAG = "CodeScreenError";
    Boolean activityOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_screen);

        final EditText codeField = findViewById(R.id.editTextCodeField);
        final Button buttonChangeSignInMethod = findViewById(R.id.buttonScanQRCode);
        final Button buttonGo = findViewById(R.id.buttonGo);

        //codeField.requestFocus();

        buttonChangeSignInMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeField.getText().toString();
                Employee emp = DatabaseController.FindEmployeeByCode(code);
                //Make sure employee was found, if so check their last clocktype and then add new one accordingly.
                if(emp != null){
                    if(emp.getAdmin()){
                        if(!activityOpen){
                            activityOpen = true;
                            startActivity(new Intent(activity_codeScreen.this, activityAdminControls.class));
                            finish();
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
        });
    }


}
