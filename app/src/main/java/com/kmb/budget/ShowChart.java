package com.kmb.budget;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

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

        ArrayList balance2 = new ArrayList();
        ArrayList balance3 = new ArrayList();


        //Below lines are just to make sure that all the data is visible.
        int assets = 0;
        int aSum = 0;
        int i = 0;
        float avg;
        for(CategorySum cs : list){
            if(cs.getType().toUpperCase().equals("Investment".toUpperCase()) || cs.getType().toUpperCase().equals("Source".toUpperCase()) || cs.getType().toUpperCase().equals("BTF".toUpperCase())){
                assets += Integer.parseInt(cs.getBalance());
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
        ArrayList balance = new ArrayList();
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
