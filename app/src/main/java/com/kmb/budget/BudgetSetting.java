package com.kmb.budget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BudgetSetting extends AppCompatActivity {

    private final Context context = this;
    CheckBox isMonthly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isMonthly = findViewById(R.id.period);
        isMonthly.setOnCheckedChangeListener(new ChangePeriod());

        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //Log.e("preference all",sharedPref.getAll().toString());
        isMonthly.setChecked(sharedPref.getBoolean(getString(R.string.showMonthly), true));

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

    class ChangePeriod implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.showMonthly),b);
                editor.apply();
                Log.e("preference file ",getString(R.string.preference_file_key));
                Log.e("preference status", Boolean.toString( sharedPref.getBoolean(getString(R.string.showMonthly), false)));
                Log.e("preference all",sharedPref.getAll().toString());

        }
    }

}
