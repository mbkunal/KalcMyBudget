package com.kmb.budget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class AddIncomeExpense extends AppCompatActivity {

    private Context context;
    private View view;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income_expense);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.context = this;
        String propName = getIntent().getStringExtra("name");
        String propType = getIntent().getStringExtra("type");
        long propValue = getIntent().getLongExtra("value",-1);
        Spinner propTypeSpinner = findViewById(R.id.propTypeValue);
        id = getIntent().getLongExtra("id",-1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prop_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propTypeSpinner.setAdapter(adapter);

        if(id != -1){
            EditText name = findViewById(R.id.propNameValue);
            name.setText(propName);
            switch(Objects.requireNonNull(propType)){
                case("Income"):
                    propTypeSpinner.setSelection(0);
                    break;
                case("Expense"):
                    propTypeSpinner.setSelection(1);
                    break;

            }
            EditText value = findViewById(R.id.propValueValue);
            value.setText(String.valueOf(propValue));
            Button bt = findViewById(R.id.createProperty);
            bt.setText("Update Property");
        }

    }
    public void addIncomeExpense(View view){
        Spinner type = findViewById(R.id.propTypeValue);
        EditText name = findViewById(R.id.propNameValue);
        EditText value = findViewById(R.id.propValueValue);
        this.view =view;
        String propName = name.getText().toString().toUpperCase();
        long propValue = Integer.parseInt(value.getText().toString());
        String propType = type.getSelectedItem().toString();
        DBClass db = new DBClass(context, this,"ADD_PROPERTY", propType, propName, propValue, id);
        db.execute();
    }

    public void status(Boolean error){
        if(error){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = this.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            String status = "Unable to create Prop. Ensure Name is unique" ;
            Snackbar transactDone = Snackbar.make(this.view, status , Snackbar.LENGTH_LONG);
            transactDone.show();
        }else{
            finish();
        }
    }


}
