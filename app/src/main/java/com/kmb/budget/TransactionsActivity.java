package com.kmb.budget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity {


    protected Transaction temp;
    private final String getTransactions = "GET_TRANSACTIONS";
    private final String deleteTransaction = "DELETE_TRANSACTION";
    Context context = this;
    TransactionsActivity ta = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new DBClass(context, ta,getTransactions).execute();
        ListView transactionListView = findViewById(R.id.transactions_listView);
        registerForContextMenu(transactionListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ListView lv = (ListView)v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)menuInfo;
        temp = (Transaction)lv.getItemAtPosition(acmi.position);
        getMenuInflater().inflate(R.menu.transaction_click_menu,menu);
    }

    public void createTransactionList(List<Transaction> list){
        Transaction header = new Transaction((long) 0,"SR","From","To","Comment","Date","Amount");
        List<Transaction> transactionsList = new ArrayList<>();
        transactionsList.add(header);
        transactionsList.addAll(list);
        ListView transactionListView = findViewById(R.id.transactions_listView);
        TransactionListAdapter tLAdapter = new TransactionListAdapter(this,R.layout.transaction_list_adapter,transactionsList);
        transactionListView.setAdapter(tLAdapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure you want to delete?");
        builder.setTitle("Permanent Delete");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DBClass(context, ta, deleteTransaction).execute();
                new DBClass(context, ta, getTransactions).execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });



        switch(item.getItemId()){
            case R.id.updateTransaction:
                Log.e("Update",temp.getTo()+temp.getComment());
                return true;
            case R.id.deleteTransaction:
                builder.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}
