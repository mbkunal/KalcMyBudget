package com.kmb.budget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class Setting extends AppCompatActivity {

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
