package com.kmb.budget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

class CategorySumListAdapter extends ArrayAdapter<CategorySum> {

    private Context context;
    private int resource;

    /**
     *
     * @param context
     * @param resource
     * @param objects
     */
    public CategorySumListAdapter(@NonNull Context context, int resource, @NonNull List<CategorySum> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getCategoryName();
        String balance =  getItem(position).getBalance();
        CategorySum cs = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView tvCName = convertView.findViewById(R.id.cs_list_cname);
        TextView tvBalance = convertView.findViewById(R.id.cs_list_balance);
        tvCName.setText(name);
        tvBalance.setText(balance);
        return convertView;
    }
}
