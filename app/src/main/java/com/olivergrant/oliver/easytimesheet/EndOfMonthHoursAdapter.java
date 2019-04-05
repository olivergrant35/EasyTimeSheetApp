package com.olivergrant.oliver.easytimesheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

public class EndOfMonthHoursAdapter extends ArrayAdapter {

    ArrayList<Employee> employees;

    public EndOfMonthHoursAdapter(Context context, ArrayList<Employee> employees){
        super(context, 0, employees);
        this.employees = employees;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Employee employee = (Employee)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_list_layout, parent, false);
        }

        TextView employeeName = (TextView)convertView.findViewById(R.id.textViewEmployeeName);
        TextView employeeHours = (TextView)convertView.findViewById(R.id.textViewEmployeeCode);

        employeeName.setText(employee.getFullname());
        try {
            employeeHours.setText(DataController.CalculateMonthsHours(employee));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
