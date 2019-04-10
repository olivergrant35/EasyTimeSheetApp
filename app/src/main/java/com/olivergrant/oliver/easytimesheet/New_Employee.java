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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class New_Employee extends AppCompatActivity {

    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern;
    private static Matcher matcher;

    private Button buttonCreateEmployee;
    private EditText editTextFistname;
    private EditText editTextSurname;
    private EditText editTextEmail;

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
                //TODO: Add email verification to ensure it is a valid email.
                if(!TextUtils.isEmpty(editTextFistname.getText()) && !TextUtils.isEmpty(editTextSurname.getText()) && !TextUtils.isEmpty(editTextEmail.getText())){
                    if (editTextFistname.getText().toString().matches("[a-zA-Z]+") && editTextSurname.getText().toString().matches("[a-zA-Z]+")) {
                        if (ValidateEmail(editTextEmail.getText().toString())) {
                            Employee emp = new Employee(editTextFistname.getText().toString(), editTextSurname.getText().toString(), editTextEmail.getText().toString());
                            DataController.WriteNewEmployee(emp);
                            Toast.makeText(New_Employee.this, "New employee created.", Toast.LENGTH_SHORT).show();
                            //SendCodeEmail(emp);
                            finish();
                        }else {
                            Toast.makeText(New_Employee.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(New_Employee.this, "Fistname and Surname can only contain letters. (a-z)", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(New_Employee.this, "All fields must be complete.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean ValidateEmail(String email){
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Send the new employee an email with their code attached.
    //TODO: Decide if I want to keep this. If so, need to get email working.
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
