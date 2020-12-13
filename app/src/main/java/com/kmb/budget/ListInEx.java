package com.kmb.budget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class ListInEx extends AppCompatActivity {

    private Context context = this;
    protected PropertyModal propertyModal;
    ListInEx listInEx = this;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_in_ex);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView aLV = findViewById(R.id.property_listProperty);
        registerForContextMenu(aLV);
        String operation = "GET_PROPERTY_LIST";
        DBClass dbclass = new DBClass(context, this, operation);
        dbclass.execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.view = v;
        ListView lv = (ListView)v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)menuInfo;
        propertyModal = (PropertyModal) lv.getItemAtPosition(acmi.position);
        getMenuInflater().inflate(R.menu.update_delete_click_menu,menu);
    }

    public void setList(List<PropertyModal> list){
        ListView aLV = findViewById(R.id.property_listProperty);
        if(list != null && list.size()>0) {
            PropertyModal temp = new PropertyModal();
            temp.setType("Type");
            temp.setPropValue((long)0);
            temp.setPropName("Name");
            list.add(0,temp);
            PropertyListAdapter cLA =  new PropertyListAdapter(context,R.layout.property_list_adapter,list);
            aLV.setAdapter(cLA);
        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure you want to delete?");
        builder.setTitle("Permanent Delete");
        builder.setPositiveButton("Delete", (dialog, id) -> {
            new DBClass(context, listInEx, "DELETE_PROPERTY").execute();
            finish();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> { });

        switch(item.getItemId()){
            case R.id.updateTransaction:
                Intent intent = new Intent(context, AddIncomeExpense.class);
                intent.putExtra("name",propertyModal.getPropName());
                intent.putExtra("type",propertyModal.getType());
                intent.putExtra("value",propertyModal.getPropValue());
                intent.putExtra("id",propertyModal.get_id());
                startActivity(intent);
                return true;
            case R.id.deleteTransaction:
                builder.show();
            return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
