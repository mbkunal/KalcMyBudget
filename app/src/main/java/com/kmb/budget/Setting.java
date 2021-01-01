package com.kmb.budget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class Setting extends AppCompatActivity {

    private final Context context = this;
    private final Activity activity = this;

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

    public void dbCleanup(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This will clean All transaction which same debit and credit category. Operation is irreversible. Are you sure you want to do this");
        builder.setTitle("Clean Transactions");
       builder.setPositiveButton("Clean", (dialog, id) -> {
           new DBClass(context,activity,"DB_CLEAN").execute();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
        });
        builder.show();
    }
}
