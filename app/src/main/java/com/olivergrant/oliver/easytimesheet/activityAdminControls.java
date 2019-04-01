package com.olivergrant.oliver.easytimesheet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activityAdminControls extends AppCompatActivity {

    Button buttonCurrentEmployees;
    Button buttonNewEmployee;
    Button buttonEndOfMonthHours;
    Button buttonOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_controls);

        buttonCurrentEmployees = findViewById(R.id.buttonCurrentEmployees);
        buttonNewEmployee = findViewById(R.id.buttonNewEmployee);
        buttonEndOfMonthHours = findViewById(R.id.buttonEndOfMonthHours);
        buttonOptions = findViewById(R.id.buttonOptions);

        buttonCurrentEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activityAdminControls.this, activity_EmployeeList.class));
            }
        });

        buttonNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activityAdminControls.this, New_Employee.class));
            }
        });

        buttonEndOfMonthHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Open page to which will display all of the employees hours for that current month.
                /*
                To display all of the hours for the current month, the hours will reset at the end of each month
                when the button is clicked, it will calculate all of the employees hours from the beginning of
                the month to the current date then display the hours next to the employees name. The admin will
                be able to click on the employee to see a breakdown of their hours.
                 */
            }
        });

        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activityAdminControls.this, activity_AdminOptions.class));
            }
        });
    }
}
