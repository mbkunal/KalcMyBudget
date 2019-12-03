package com.kmb.budget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.validation.Validator;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String INVESTMENT= "INVESTMENT";
    public static final String EXPENDITURE = "EXPENDITURE";
    public static final String SOURCE = "SOURCE";
    public static final String BTF = "BTF";

    MainDatabase db ;
    CategoryDAO categoryDAO;  // = db.categoryDAO();
    TransactionDAO transactionDAO;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = MainDatabase.getMainDatabase(this.getApplicationContext());
        categoryDAO = db.categoryDAO();
        transactionDAO =db.transactionDAO();
        DBClass db = new DBClass(this, this,"GET_CATEGORIES");
        db.execute();
    }
    public void setList(List<String> list){
        List<String >categoryNamesList = list;
        if(list != null) {
            Log.i("Categories", categoryNamesList.get(1));
            String[] categoryNamesArray = new String[categoryNamesList.size()];
            categoryNamesArray = categoryNamesList.toArray(categoryNamesArray);
            ArrayAdapter<String> categories = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryNamesArray);
            ((Spinner) findViewById(R.id.fromDropdown)).setAdapter(categories);
            ((Spinner) findViewById(R.id.toDropdown)).setAdapter(categories);
        }
    }
    public void addTransaction(View view) {
        Spinner toCategory = findViewById(R.id.toDropdown);
        Spinner fromCategory = findViewById(R.id.fromDropdown);
        EditText comment = findViewById(R.id.comment);
        Button date = findViewById(R.id.transDate);
        int amount = Integer.valueOf(((EditText)findViewById(R.id.amount)).getText().toString());
        String to = toCategory.getSelectedItem().toString();
        String from = fromCategory.getSelectedItem().toString();
        String commentText = comment.getText().toString();
        Date tDate = new Date();
        Date currentDate = new Date();
        try {
            tDate = formatter.parse(date.getText().toString());
        } catch (ParseException e) {}
        DBClass dbclass = new DBClass(this,to,from,commentText,amount,currentDate,tDate);
        dbclass.execute();
        Snackbar transactDone = Snackbar.make(view, "Transaction Added", Snackbar.LENGTH_SHORT);
        transactDone.show();
    }
    public void showDatePickerDialog(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void goToAnalysis(View view){
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
    public void addSalary(View view) {
        /*TODO
        Add salary as a transaction in db
         */
        Snackbar mySnackbar = Snackbar.make(view, "Salary Added", Snackbar.LENGTH_SHORT);
        mySnackbar.show();

    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        ((Button)findViewById(R.id.transDate)).setText(day + "/" + month + "/" + year);
    }
}





