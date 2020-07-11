package com.kmb.budget;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.createColors;

public class ShowChart extends AppCompatActivity {

    Context context;
    static PieChart pChart;
    static List<Integer> colors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int[] rColors = {R.color.chartBlue,R.color.chartBrown,R.color.chartGreen,R.color.chartOrange,R.color.chartPink,R.color.chartPurple,R.color.chartRed,R.color.chartYellow};
        context = this;
        pChart = findViewById(R.id.pie_chart);
        DBClass dbclass = new DBClass(this, this,"GET_ANALYSIS_FOR_CHART");
        dbclass.execute();
        colors = ColorTemplate.createColors(getResources(),rColors);
        //showChart(ArrayList<null>);


    }
    public static void showChart(List<CategorySum> list){

        ArrayList balance = new ArrayList();
        for(CategorySum cs : list){
            if(cs.getType().toUpperCase().equals("Investment".toUpperCase()) || cs.getType().toUpperCase().equals("Source".toUpperCase()) || cs.getType().toUpperCase().equals("BTF".toUpperCase())){
                balance.add(new PieEntry(Integer.parseInt(cs.getBalance()),cs.getCategoryName()));
            }
        }/*
        balance.add(new PieEntry(10f,"Mutual Fund"));
        balance.add(new PieEntry(20f,"BTF"));
        balance.add(new PieEntry(30f,"Equity"));
        balance.add(new PieEntry(40f,"Cash"));
        balance.add(new PieEntry(50f,"Bank"));
        balance.add(new PieEntry(60f,"FD"));*/
        PieDataSet dataSet = new PieDataSet(balance, "Balance");

        PieData data = new PieData(dataSet);
        pChart.setData(data);
        dataSet.setColors(colors);
        pChart.animateXY(1000, 1000);


    }

}
