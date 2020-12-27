package com.kmb.budget;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ShowChart extends AppCompatActivity {

    Context context;
    static PieChart pChart;
    static List<Integer> colors;
    static TextView monthlyBudgetText;
    static TextView budgetLeftText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        int[] rColors = {R.color.chartBlue,R.color.chartBrown,R.color.chartGreen,R.color.chartOrange,R.color.chartPink,R.color.chartPurple,R.color.chartRed,R.color.chartYellow};
        context = this;
        pChart = findViewById(R.id.pie_chart);
        monthlyBudgetText = findViewById(R.id.monthlyBudgetText);
        budgetLeftText = findViewById(R.id.BudgetLeftText);
        DBClass dbclass = new DBClass(this, this,"GET_ANALYSIS_FOR_CHART");
        dbclass.execute();
        colors = ColorTemplate.createColors(getResources(),rColors);
        //showChart(ArrayList<null>);


    }
    public static void showChart(List<CategorySum> list, long monthlyBudget, long budgetLeft){

        ArrayList balance = new ArrayList();
        int assets = 0;
        int i = 0;
        monthlyBudgetText.setText("Monthly Budget :-  " + (monthlyBudget));
        budgetLeftText.setText("Budget Left :-  " + (budgetLeft));



        while(i<list.size()){
            CategorySum cs = list.get(i);
            if(cs.getType().toUpperCase().equals(MainActivity.EXPENDITURE.toUpperCase())){
                list.remove(cs);
            } else if (Float.parseFloat(cs.getBalance()) <= 0  ){
                assets += Integer.parseInt(cs.getBalance());
                list.remove(cs);
            }else{
                assets += Integer.parseInt(cs.getBalance());
                i++;
            }
        }
        Collections.sort(list);
        //balance.add(new PieEntry(Float.parseFloat(cs.getBalance()), cs.getCategoryName()));
        while(!list.isEmpty()){
            CategorySum cs = list.remove(0);
            balance.add(new PieEntry(Float.parseFloat(cs.getBalance()), cs.getCategoryName()));
            if(list.size()>0){
                cs = list.remove(list.size()-1);
                balance.add(new PieEntry(Float.parseFloat(cs.getBalance()), cs.getCategoryName()));
            }
        }
        // END OPTIMIZER

        PieDataSet dataSet = new PieDataSet(balance, "Balance");

        PieData data = new PieData(dataSet);
        pChart.setData(data);
        Description desc = new Description();
        desc.setText("Total assets " + assets);
        pChart.setDescription(desc);
        pChart.setEntryLabelTextSize(12);
        pChart.getDescription().setTextSize(17);
        pChart.getLegend().setWordWrapEnabled(true);
        dataSet.setColors(colors);
        pChart.animateXY(1000, 1000);
    }
}
