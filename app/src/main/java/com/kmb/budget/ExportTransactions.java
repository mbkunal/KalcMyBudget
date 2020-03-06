package com.kmb.budget;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExportTransactions extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener{

    private int callerId;
    private String me = "EXPORT_TRANSACTIONS";
    private Context context = this;
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private static int permissionFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DBClass db = new DBClass(this, this,"GET_CATEGORIES");
        db.setCaller(me);
        db.execute();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }else{
            permissionFlag = 1;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void showDatePickerDialog(View view){
        callerId = view.getId();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCaller(callerId);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void exportTransactions(View view) throws ParseException {
        //Get values from UI
        EditText fromDate = findViewById(R.id.fromDateValue);
        EditText toDate = findViewById(R.id.toDateValue);
        Date fromDateValue = formatter.parse(fromDate.getText().toString());
        Date toDateValue = formatter.parse(toDate.getText().toString());
        String category = ((Spinner)findViewById(R.id.filterCategoryValue))
                .getSelectedItem().toString();

        // transfer data to transactions activity
        Intent intent = new Intent(context, TransactionsActivity.class);
        intent.putExtra("FilteredListFromExport",true);
        intent.putExtra("fromDate", Converters.dateToTimestamp(fromDateValue));
        intent.putExtra("toDate", Converters.dateToTimestamp(toDateValue));
        intent.putExtra("categoryName", category);
        startActivity(intent);
    }

    public static void createExcel(List<Transaction> list) {

        if(permissionFlag!=1){
            return;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow heading = sheet.createRow(0);
        heading.createCell(0).setCellValue("Sr. No");
        heading.createCell(1).setCellValue("From");
        heading.createCell(2).setCellValue("To");
        heading.createCell(3).setCellValue("Comment");
        heading.createCell(4).setCellValue("Amount");
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
        sheet.setColumnWidth(0,1700);
        sheet.setColumnWidth(1,4000);
        sheet.setColumnWidth(2,4000);
        sheet.setColumnWidth(3,6000);
        sheet.setColumnWidth(4,2000);
        Date date = new Date();
        DateFormat f = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        String filename = "transaction"+f.format(date) +".xlsx";
        String directory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(directory,"Export");
        folder.mkdir();
        File file = new File(folder,filename);
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setList(List<String> list){
        List<String >categoryNamesList = new ArrayList<>();
        categoryNamesList.add("ALL");
        categoryNamesList.addAll(list);
        if(list != null && list.size()>0) {

            String[] categoryNamesArray = new String[categoryNamesList.size()];
            categoryNamesArray = categoryNamesList.toArray(categoryNamesArray);
            final ArrayAdapter<String> categories = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryNamesArray);
            ((Spinner) findViewById(R.id.filterCategoryValue)).setAdapter(categories);
        }
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month+1;
        ((EditText)findViewById(callerId)).setText(day + "/" + month + "/" + year);
    }
}
