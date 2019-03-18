package com.olivergrant.oliver.easytimesheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class New_Employee extends AppCompatActivity {

    Button buttonCreateEmployee;
    EditText editTextFistname;
    EditText editTextSurname;
    DatabaseController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__employee);

        db = new DatabaseController();

        buttonCreateEmployee = findViewById(R.id.buttonCreateEmployee);
        editTextFistname = findViewById(R.id.editTextFirstname);
        editTextSurname = findViewById(R.id.editTextSurname);

        buttonCreateEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Need to check to make sure the names only contain letters.
                if(!TextUtils.isEmpty(editTextFistname.getText()) && !TextUtils.isEmpty(editTextSurname.getText())){
                    db.WriteNewEmployee(new Employee(editTextFistname.getText().toString(), editTextSurname.getText().toString()));
                }
            }
        });
    }
}
