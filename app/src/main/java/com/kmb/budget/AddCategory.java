package com.kmb.budget;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner catTypeSpinner = findViewById(R.id.categoryTypeValue);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catTypeSpinner.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void createCategory(View view){
        Spinner type = findViewById(R.id.categoryTypeValue);
        EditText name = findViewById(R.id.categoryNameValue);
        String nm = name.getText().toString();
        String tp = type.getSelectedItem().toString();

        DBClass db = new DBClass(this,nm,tp);
        db.execute();
        String status = "Category"+nm+"Created" ;
        Snackbar transactDone = Snackbar.make(view, status , Snackbar.LENGTH_SHORT);
        transactDone.show();
        finish();

    }

}
