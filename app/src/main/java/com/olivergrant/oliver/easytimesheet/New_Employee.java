package com.olivergrant.oliver.easytimesheet;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class New_Employee extends AppCompatActivity {

    Button buttonCreateEmployee;
    EditText editTextFistname;
    EditText editTextSurname;
    EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__employee);

        buttonCreateEmployee = findViewById(R.id.buttonCreateEmployee);
        editTextFistname = findViewById(R.id.editTextFirstname);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextEmail = findViewById(R.id.editTextEmail);

        buttonCreateEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Need to check to make sure the names only contain letters.
                if(!TextUtils.isEmpty(editTextFistname.getText()) && !TextUtils.isEmpty(editTextSurname.getText())){
                    Employee emp = new Employee(editTextFistname.getText().toString(), editTextSurname.getText().toString(), editTextEmail.getText().toString());
                    DataController.WriteNewEmployee(emp);
                    Toast.makeText(New_Employee.this, "New employee created.", Toast.LENGTH_SHORT).show();
                    //SendCodeEmail(emp);
                    finish();
                }
            }
        });
    }

    //Send the new employee an email with their code attached.
    public void SendCodeEmail(Employee emp){
        String[] TO = {"olivergrant35@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your QR Code");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "You should find your QR code attached.");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Log.d("NewEmployeeError", ex.getMessage());
        }
    }
}
