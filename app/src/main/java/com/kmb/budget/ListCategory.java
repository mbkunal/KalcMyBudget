package com.kmb.budget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

import java.util.List;

public class ListCategory extends AppCompatActivity {

    private Context context = this;
    protected CategoryModal categoryModal;
    private final String operation = "GET_CATEGORY_LIST";
    ListCategory lC = this;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ListView aLV = findViewById(R.id.category_ListCategory);
        registerForContextMenu(aLV);
        DBClass dbclass = new DBClass(context, this,operation);
        dbclass.execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.view = v;
        ListView lv = (ListView)v;
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)menuInfo;
        categoryModal = (CategoryModal) lv.getItemAtPosition(acmi.position);
        getMenuInflater().inflate(R.menu.update_delete_click_menu,menu);
    }

    public void setList(List<CategoryModal> list){
        List<CategoryModal>categoryNamesList = list;
        ListView aLV = findViewById(R.id.category_ListCategory);
        if(list != null && list.size()>0) {
            ListView CategoryListView = findViewById(R.id.category_ListCategory);
            CategoryListAdapter cLA =  new CategoryListAdapter(context,R.layout.category_list_adapter,list);
            aLV.setAdapter(cLA);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure you want to delete?");
        builder.setTitle("Permanent Delete");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(categoryModal.getCategoryName().equals("Sink")){
                    Snackbar opFailed = Snackbar.make(view,"Cannot delete Sink",Snackbar.LENGTH_SHORT);
                    opFailed.show();
                }
                else {
                    new DBClass(context, lC, "DELETE_CATEGORY").execute();
                }
                //new DBClass(context, ta, getTransactions).execute();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });



        switch(item.getItemId()){
            case R.id.updateTransaction:
                Intent intent = new Intent(context, AddCategory.class);
                intent.putExtra("name",categoryModal.getCategoryName());
                intent.putExtra("type",categoryModal.getType());
                intent.putExtra("id",categoryModal.getId());
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
