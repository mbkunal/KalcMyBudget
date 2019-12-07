package com.kmb.budget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String INVESTMENT= "INVESTMENT";
    public static final String EXPENDITURE = "EXPENDITURE";
    public static final String SOURCE = "SOURCE";
    public static final String BTF = "BTF";

    private MainDatabase db ;
    private CategoryDAO categoryDAO;  // = db.categoryDAO();
    private TransactionDAO transactionDAO;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = MainDatabase.getMainDatabase(this.getApplicationContext());
        categoryDAO = db.categoryDAO();
        transactionDAO =db.transactionDAO();
        EditText edt = findViewById(R.id.transDate);
        edt.setText(formatter.format(new Date()));
        edt.setShowSoftInputOnFocus(false);
        DBClass db = new DBClass(this, this,"GET_CATEGORIES");
        db.execute();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        ((EditText)findViewById(R.id.transDate)).setText(day + "/" + month + "/" + year);
    }
    @Override
    public void onResume() {
        DBClass db = new DBClass(this, this,"GET_CATEGORIES");
        db.execute();
        super.onResume();
    }
    public void setList(List<String> list){
        List<String >categoryNamesList = list;
        if(list != null && list.size()>0) {
            String[] categoryNamesArray = new String[categoryNamesList.size()];
            categoryNamesArray = categoryNamesList.toArray(categoryNamesArray);
            ArrayAdapter<String> categories = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryNamesArray);
            ((Spinner) findViewById(R.id.fromDropdown)).setAdapter(categories);
            ((Spinner) findViewById(R.id.toDropdown)).setAdapter(categories);
        }
    }
    public void addTransaction(View view) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(this.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
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
        Date tDate;
        Date currentDate = new Date();
        /*DATE BLOCK*/try {
            tDate = formatter.parse(date.getText().toString());
        } catch (ParseException e) {
            tDate = currentDate;
        }
        if(to.equals("") || from.equals("")|| commentText.equals("") ){
            return;
        }
        DBClass dbclass = new DBClass(this, to, from, commentText, amount, currentDate, tDate);
        dbclass.execute();
        Snackbar transactDone = Snackbar.make(view, "Transaction Added", Snackbar.LENGTH_SHORT);
        transactDone.show();
        comment.setText("");
        ((EditText) findViewById(R.id.amount)).setText("");
        date.setText(formatter.format(new Date()));
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
        Snackbar salary = Snackbar.make(view, "Salary Added", Snackbar.LENGTH_SHORT);
        salary.show();

    }

}





