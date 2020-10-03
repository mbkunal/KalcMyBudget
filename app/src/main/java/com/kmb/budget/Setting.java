package com.kmb.budget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class Setting extends AppCompatActivity {

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
    public void addCategory(View view){
        Intent intent = new Intent(context, AddCategory.class);
        startActivity(intent);
    }

    public void listCategory(View view) {
        Intent intent = new Intent(context, ListCategory.class);
        startActivity(intent);
    }

    public void exportTransactions(View view) {
        Intent intent = new Intent(context, ExportTransactions.class);
        startActivity(intent);
    }
    public void budgetSetting(View view) {
        Intent intent = new Intent(context, BudgetSetting.class);
        startActivity(intent);
    }

}
