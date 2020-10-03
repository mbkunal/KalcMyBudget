package com.kmb.budget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BudgetSetting extends AppCompatActivity {

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void addIncomeExpense(View view) {
        Intent intent = new Intent(context, AddIncomeExpense.class);
        startActivity(intent);
    }
    public void listInEx(View view) {
        Intent intent = new Intent(context, ListInEx.class);
        startActivity(intent);
    }

}
