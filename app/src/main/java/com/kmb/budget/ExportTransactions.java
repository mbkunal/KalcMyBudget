package com.kmb.budget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ExportTransactions extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener{

    private int callerId;
    private Date maxDate;
    private Date minDate;
    private Context context = this;
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_export_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        maxDate = new Date();
        DBClass db = new DBClass(this, this,"GET_CATEGORIES");
        String me = "EXPORT_TRANSACTIONS";
        EditText edt = findViewById(R.id.fromDateValue);
        edt.setText(formatter.format(new Date()));
        edt.setShowSoftInputOnFocus(false);
        edt = findViewById(R.id.toDateValue);
        edt.setText(formatter.format(new Date()));
        edt.setShowSoftInputOnFocus(false);
        db.setCaller(me);
        db.execute();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    public void showDatePickerDialog(View view){
        callerId = view.getId();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCaller(callerId);
        if(R.id.fromDateValue == callerId){
            datePickerFragment.setMinDate(null);
            datePickerFragment.setMaxDate(this.maxDate);
        }else if (R.id.toDateValue == callerId){
            datePickerFragment.setMaxDate(new Date());
            datePickerFragment.setMinDate(this.minDate);
        }
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void exportTransactions(View view) throws ParseException {
        //Get values from UI
        EditText fromDate = findViewById(R.id.fromDateValue);
        EditText toDate = findViewById(R.id.toDateValue);
        Date fromDateValue = formatter.parse(fromDate.getText().toString());
        Date toDateValue = formatter.parse(toDate.getText().toString());
        String category = ((Spinner)findViewById(R.id.filterCategoryValue))
                .getSelectedItem().toString();

        // transfer data to transactions activity
        Intent intent = new Intent(context, TransactionsActivity.class);
        intent.putExtra("FilteredListFromExport",true);
        intent.putExtra("fromDate", Converters.dateToTimestamp(fromDateValue));
        intent.putExtra("toDate", Converters.dateToTimestamp(toDateValue));
        intent.putExtra("categoryName", category);
        startActivity(intent);
    }

    public void setList(List<String> list){
        List<String >categoryNamesList = new ArrayList<>();
        categoryNamesList.add("ALL");
        categoryNamesList.addAll(list);
        if(list.size() > 0) {

            String[] categoryNamesArray = new String[categoryNamesList.size()];
            categoryNamesArray = categoryNamesList.toArray(categoryNamesArray);
            final ArrayAdapter<String> categories = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryNamesArray);
            ((Spinner) findViewById(R.id.filterCategoryValue)).setAdapter(categories);
        }
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month+1;
        ((EditText)findViewById(callerId)).setText(day + "/" + month + "/" + year);
        if(R.id.fromDateValue == callerId){
            try {
                this.minDate = new SimpleDateFormat("dd/MM/yyyy").parse(day+"/"+ month+ "/"+year);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if (R.id.toDateValue == callerId){
            try {
                this.maxDate = new SimpleDateFormat("dd/MM/yyyy").parse(day+"/"+ month+ "/"+year);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
