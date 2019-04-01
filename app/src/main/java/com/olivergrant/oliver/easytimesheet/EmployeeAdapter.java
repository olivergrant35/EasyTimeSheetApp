package com.olivergrant.oliver.easytimesheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployeeAdapter  extends ArrayAdapter implements Filterable {

    ArrayList<Employee> employees;

    public EmployeeAdapter(Context context, ArrayList<Employee> employees){
        super(context, 0, employees);
        employees = employees;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        Employee employee = (Employee)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_list_layout, parent, false);
        }

        TextView employeeName = (TextView)convertView.findViewById(R.id.textViewEmployeeName);
        TextView employeeCode = (TextView)convertView.findViewById(R.id.textViewEmployeeCode);

        employeeName.setText(employee.getFullname());
        employeeCode.setText(employee.getEmployeeCode());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Employee> FilteredEmployees = new ArrayList<>();

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < DataController.getEmployeeList().size(); i++){
                    String empName = DataController.getEmployeeList().get(i).getFullname();
                    if(empName.toLowerCase().startsWith(constraint.toString())){
                        FilteredEmployees.add(DataController.getEmployeeList().get(i));
                    }
                }
                results.count = FilteredEmployees.size();
                results.values = FilteredEmployees;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                employees = (ArrayList<Employee>)results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
