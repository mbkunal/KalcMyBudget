package com.kmb.budget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String INVESTMENT= "Investment";
    public static final String EXPENDITURE = "Expenditure";
    public static final String SOURCE = "Source";
    public static final String BTF = "BTF";
    Spinner budgetTransactionType;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");


        budgetTransactionType = findViewById(R.id.budgetTransactionType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prop_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        CheckBox isBudgetView = findViewById(R.id.isBudget);
        isBudgetView.setOnClickListener(view1 -> {
            if(isBudgetView.isChecked()){
                budgetTransactionType = findViewById(R.id.budgetTransactionType);
                budgetTransactionType.setVisibility(View.VISIBLE);
            }else{
                budgetTransactionType = findViewById(R.id.budgetTransactionType);
                budgetTransactionType.setVisibility(View.INVISIBLE);
            }
        });


        budgetTransactionType.setAdapter(adapter);
        EditText edt = findViewById(R.id.transDate);
        edt.setText(formatter.format(new Date()));
        edt.setShowSoftInputOnFocus(false);
        DBClass db = new DBClass(this, this,"GET_CATEGORIES");
        db.execute();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month+1;
        ((EditText)findViewById(R.id.transDate)).setText(day + "/" + month + "/" + year);
    }
    @Override
    public void onResume() {
        DBClass db = new DBClass(this, this,"GET_CATEGORIES");
        db.execute();
        super.onResume();
    }
    public void setList(List<String> list){
        //Called after db.execute in onCreate
        if(list != null && list.size()>0) {
            String[] categoryNamesArray = new String[list.size()];
            categoryNamesArray = list.toArray(categoryNamesArray);
            final ArrayAdapter<String> categories = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryNamesArray);
            ((Spinner) findViewById(R.id.fromDropdown)).setAdapter(categories);
            ((Spinner) findViewById(R.id.toDropdown)).setAdapter(categories);
        }
    }
    public void addTransaction(View view) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(this.INPUT_METHOD_SERVICE);

        Objects.requireNonNull(inputManager).hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        Spinner toCategory = findViewById(R.id.toDropdown);
        Spinner fromCategory = findViewById(R.id.fromDropdown);
        EditText comment = findViewById(R.id.comment);
        EditText date = findViewById(R.id.transDate);
        int amount;
        String to;
        String from;
        try {
            amount = Integer.valueOf(((EditText) findViewById(R.id.amount)).getText().toString());
            to = toCategory.getSelectedItem().toString();
            from = fromCategory.getSelectedItem().toString();
        }catch(Exception e){
            Snackbar invalidValue = Snackbar.make(view, "Check the values", Snackbar.LENGTH_LONG);
            invalidValue.show();
            return;
        }
        String commentText = comment.getText().toString();
        commentText = commentText.replaceAll(";",",");
        Date tDate;
        Date currentDate = new Date();
        /*DATE BLOCK*/
        try {
            tDate = formatter.parse(date.getText().toString());
        } catch (ParseException e) {
            tDate = currentDate;
        }
        if(to.equals("") || from.equals("")|| commentText.equals("") ){
            Snackbar invalidValue = Snackbar.make(view, "Check the values", Snackbar.LENGTH_LONG);
            invalidValue.show();
            return;
        }
        CheckBox isBudgetView = findViewById(R.id.isBudget);
        Boolean isbudget = isBudgetView.isChecked();
        if(isbudget){
            commentText = commentText + ";" +budgetTransactionType.getSelectedItem().toString();
        }
        DBClass dbclass = new DBClass(this, to, from, commentText, amount, currentDate, tDate,isbudget);
        dbclass.execute();
        Snackbar transactDone = Snackbar.make(view, "Transaction Added", Snackbar.LENGTH_SHORT);
        transactDone.show();
        comment.setText("");
        if(isBudgetView.isChecked()){
            isBudgetView.toggle();
        }
        ((EditText) findViewById(R.id.amount)).setText("");
        date.setText(formatter.format(new Date()));
    }
    public void showDatePickerDialog(View view){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCaller(-123);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void goToBalanceSheet(View view){
        Intent intent = new Intent(this, AnalysisActivity.class);
        startActivity(intent);
    }
    public void goToSettings(View view){
       Intent intent = new Intent(this, Setting.class);
       startActivity(intent);

    }
    public void goToTransactions(View view){
        Intent intent = new Intent(this, TransactionsActivity.class);
        startActivity(intent);

    }
    public void showAnalysis(View view) {
        Intent intent = new Intent(this, ShowChart.class);
        startActivity(intent);

        Snackbar salary = Snackbar.make(view, "Salary Added", Snackbar.LENGTH_SHORT);
        salary.show();

    }

}





