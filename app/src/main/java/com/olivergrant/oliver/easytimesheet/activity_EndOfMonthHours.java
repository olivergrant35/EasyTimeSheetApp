package com.olivergrant.oliver.easytimesheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class activity_EndOfMonthHours extends AppCompatActivity {

    EndOfMonthHoursAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__end_of_month_hours);

        adapter = new EndOfMonthHoursAdapter(this, DataController.getEmployeeList());

        ListView listView = findViewById(R.id.listViewEmployeeList);
        listView.setAdapter(adapter);
    }
}
