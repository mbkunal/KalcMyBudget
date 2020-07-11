package com.kmb.budget;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
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

import static com.github.mikephil.charting.components.Legend.LegendPosition.ABOVE_CHART_CENTER;
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
        ArrayList balance2 = new ArrayList();
        ArrayList balance3 = new ArrayList();


        //Below lines are just to make sure that all the data is visible.
        int aSum = 0;
        int i = 0;
        float avg = 0;
        for(CategorySum cs : list){
            if(cs.getType().toUpperCase().equals("Investment".toUpperCase()) || cs.getType().toUpperCase().equals("Source".toUpperCase()) || cs.getType().toUpperCase().equals("BTF".toUpperCase())){
                if( Integer.parseInt(cs.getBalance()) > 0) {
                    aSum += Integer.parseInt(cs.getBalance());
                    i++;
                }
            }
        }
        try {
            avg = aSum / i;
        }catch (Exception e){
            avg = 0;
            e.printStackTrace();
        }
        for(CategorySum cs : list){
            if(cs.getType().toUpperCase().equals("Investment".toUpperCase()) || cs.getType().toUpperCase().equals("Source".toUpperCase()) || cs.getType().toUpperCase().equals("BTF".toUpperCase())){
                if( Integer.parseInt(cs.getBalance()) > 0) {
                    if(Float.parseFloat((cs.getBalance())) >  avg){
                        balance.add(new PieEntry(Float.parseFloat(cs.getBalance()), cs.getCategoryName()));
                    }else{
                        balance2.add(new PieEntry(Float.parseFloat(cs.getBalance()), cs.getCategoryName()));
                    }

                    Log.e("cs.getCategoryName()", String.valueOf(Float.parseFloat(cs.getBalance())));
                }
            }
        }
        if (balance.size() > balance2.size()){
            while(balance2.size()>0) {
                balance3.add(balance.remove(0));
                balance3.add(balance2.remove(0));
            }
            balance3.addAll(balance);
        }else
        {
            while(balance.size()>0) {
                balance3.add(balance2.remove(0));
                balance3.add(balance.remove(0));
            }
            balance3.addAll(balance2);
        }
        PieDataSet dataSet = new PieDataSet(balance3, "Balance");

        PieData data = new PieData(dataSet);
        pChart.setData(data);
        dataSet.setColors(colors);
        pChart.animateXY(1000, 1000);
    }

}
