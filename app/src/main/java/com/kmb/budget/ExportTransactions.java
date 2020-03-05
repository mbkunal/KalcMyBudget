package com.kmb.budget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExportTransactions extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{

    private int callerId;
    private String me = "EXPORT_TRANSACTIONS";
    private Context context = this;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DBClass db = new DBClass(this, this,"GET_CATEGORIES");
        db.setCaller(me);
        db.execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void showDatePickerDialog(View view){
        callerId = view.getId();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCaller(callerId);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void exportTransactions(View view) throws ParseException {
        EditText fromDate = findViewById(R.id.fromDateValue);
        EditText toDate = findViewById(R.id.toDateValue);
        Date fromDateValue = formatter.parse(fromDate.getText().toString());
        Date toDateValue = formatter.parse(toDate.getText().toString());
        String category = ((Spinner)findViewById(R.id.filterCategoryValue)).getSelectedItem().toString();
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
        if(list != null && list.size()>0) {

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
    }
}
