package com.kmb.budget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnalysisActivity extends AppCompatActivity {

    private Context context = this;
    CategorySum categorySum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ListView aLV = findViewById(R.id.analysis_listview);
        aLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                categorySum = (CategorySum) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(context, TransactionsActivity.class);
                intent.putExtra("category",categorySum.getId());
                startActivity(intent);
            }

        });
        DBClass dbclass = new DBClass(this, this,"GET_ANALYSIS");
        dbclass.execute();
    }

    public void createAnalysisList(List<CategorySum> list) {
        CategorySum header = new CategorySum((long) 0,"Category","Balance","Type");
        List<CategorySum> csl = new ArrayList<>();
        csl.add(header);
        for(CategorySum temp: list){
            if(!temp.getCategoryName().toUpperCase().equals("Sink".toUpperCase())){
                csl.add(temp);
            }
        }
        ListView analysisListView = findViewById(R.id.analysis_listview);
        CategorySumListAdapter csla = new CategorySumListAdapter(this,R.layout.analysis_list_adapter,csl);
        analysisListView.setAdapter(csla);
    }

}
