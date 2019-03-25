package com.olivergrant.oliver.easytimesheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class activity_EmployeeList extends AppCompatActivity {

    EmployeeAdapter adapter;
    TextView textViewNumofEmployees;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__employee_list);

        textViewNumofEmployees = (TextView)findViewById(R.id.textViewNumOfEmployees);
        searchBar = (EditText)findViewById(R.id.editTextEmployeeSearchBar);

        //TODO: Need to make searching the employee list working. 
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String message = getString(R.string.employeeListTotal);
        textViewNumofEmployees.setText(message + " " + DatabaseController.getEmployeeList().size());

        adapter = new EmployeeAdapter(this, DatabaseController.getEmployeeList());

        ListView listView = (ListView)findViewById(R.id.listViewEmployees);
        listView.setAdapter(adapter);
    }
}
