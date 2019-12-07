package com.kmb.budget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DBClass dbclass = new DBClass(this, this,"GET_ANALYSIS");
        dbclass.execute();
    }

    public void createAnalysisList(List<CategorySum> list) {
        CategorySum header = new CategorySum("Category","Balance");
        List<CategorySum> csl = new ArrayList<>();
        csl.add(header);
        csl.addAll(list);
        ListView analysisListView = findViewById(R.id.analysis_listview);
        CategorySumListAdapter csla = new CategorySumListAdapter(this,R.layout.analysis_list_adapter,csl);
        analysisListView.setAdapter(csla);
    }
}
