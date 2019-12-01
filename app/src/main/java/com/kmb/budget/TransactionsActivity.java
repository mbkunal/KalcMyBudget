package com.kmb.budget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DBClass dbclass = new DBClass(this, this,"GET_TRANSACTIONS");
        dbclass.execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void createTransactionList(List<Transaction> list){
        Transaction header = new Transaction("SR","From","To","Comment","Date","Amount");
        List<Transaction> temp = new ArrayList<>();
        temp.add(header);
        temp.addAll(list);
        ListView transactionListView = findViewById(R.id.transactions_listView);
        List<Transaction> transactionsList = temp;
        TransactionListAdapter tLAdapter = new TransactionListAdapter(this,R.layout.transaction_list_adapter,transactionsList);
        transactionListView.setAdapter(tLAdapter);
    }



}
