package com.kmb.budget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TransactionsActivity extends AppCompatActivity {


    protected Transaction temp;
    private final String deleteTransaction = "DELETE_TRANSACTION";
    Context context = this;
    private String category ;
    TransactionsActivity transactionsActivity = this;
    private boolean createExcel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        boolean isListGiven = getIntent().getBooleanExtra("FilteredListFromExport",false);
        String getTransactions = "GET_TRANSACTIONS";
        if(!isListGiven) {
            Long filterCategoryId = getIntent().getLongExtra("category", -1);
            new DBClass(context, transactionsActivity, getTransactions, filterCategoryId).execute();
        }else{
            this.createExcel = true;
            Long fromDate = getIntent().getLongExtra("fromDate", 0);
            Long toDate = getIntent().getLongExtra("toDate", 0);
            String category = getIntent().getStringExtra("categoryName");
            this.category = category;
            new DBClass(context, transactionsActivity, getTransactions +"Filtered", fromDate,toDate,category).execute();
        }
        ListView transactionListView = findViewById(R.id.transactions_listView);
        registerForContextMenu(transactionListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ListView lv = (ListView)v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)menuInfo;
        temp = (Transaction)lv.getItemAtPosition(acmi.position);
        getMenuInflater().inflate(R.menu.update_delete_click_menu,menu);
    }

    public void createTransactionList(List<Transaction> list){
        if(createExcel){
            createExcel(list);
        }
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
        builder.setPositiveButton("Delete", (dialog, id) -> {
            new DBClass(context, transactionsActivity, deleteTransaction).execute();
            //new DBClass(context, ta, getTransactions).execute();
            finish();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
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

    public void createExcel(List<Transaction> list) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 121);

        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow heading = sheet.createRow(0);
        heading.createCell(0).setCellValue("Sr. No");
        heading.createCell(1).setCellValue("From");
        heading.createCell(2).setCellValue("To");
        heading.createCell(3).setCellValue("Comment");
        heading.createCell(4).setCellValue("Amount");

        if(!category.toUpperCase().equals("all".toUpperCase())){
            long totalTo = 0 ;
            long totalFrom = 0 ;
            int i=1;
            for(Transaction t : list){
                XSSFRow temp = sheet.createRow(i);
                temp.createCell(0).setCellValue(t.getSr());
                temp.createCell(1).setCellValue(t.getFrom());
                temp.createCell(2).setCellValue(t.getTo());
                temp.createCell(3).setCellValue(t.getComment());
                temp.createCell(4).setCellValue(t.getAmount());
                i++;
                if(t.getTo().equals(category))
                {
                    totalTo +=Long.parseLong(t.getAmount());
                }else if(t.getFrom().equals(category)){
                    totalFrom += Long.parseLong(t.getAmount());
                }
            }
            XSSFRow temp = sheet.createRow(i);
            temp.createCell(1).setCellValue("Total in");
            temp.createCell(2).setCellValue(totalTo);
            temp = sheet.createRow(i+1);
            temp.createCell(1).setCellValue("Total Out");
            temp.createCell(2).setCellValue(totalFrom);
            temp = sheet.createRow(i+2);
            temp.createCell(1).setCellValue("Balance");
            temp.createCell(2).setCellValue(totalTo-totalFrom);

        }else{
            int i=1;
            for(Transaction t : list){
                XSSFRow temp = sheet.createRow(i);
                temp.createCell(0).setCellValue(t.getSr());
                temp.createCell(1).setCellValue(t.getFrom());
                temp.createCell(2).setCellValue(t.getTo());
                temp.createCell(3).setCellValue(t.getComment());
                temp.createCell(4).setCellValue(t.getAmount());
                i++;
            }
        }
        sheet.setColumnWidth(0,1700);
        sheet.setColumnWidth(1,4000);
        sheet.setColumnWidth(2,4000);
        sheet.setColumnWidth(3,6000);
        sheet.setColumnWidth(4,2000);
        Date date = new Date();
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        String filename = "transaction"+f.format(date) +".xlsx";
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Transaction");
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file = new File(folder,filename);
        if(file.exists()){
            int i=0;
            while(file.exists()) {
                i++;
                filename = "transaction" + f.format(date) + "(" +i +").xlsx";
                file = new File(folder,filename);
            }
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "file created", Toast.LENGTH_LONG).show();
            Log.e("File","created");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
