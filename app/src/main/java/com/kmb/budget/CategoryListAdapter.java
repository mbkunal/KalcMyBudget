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
import java.util.Objects;

class CategoryListAdapter extends ArrayAdapter<CategoryModal> {

    private Context context;
    private int resource;


    public CategoryListAdapter(@NonNull Context context, int resource, @NonNull List<CategoryModal> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = Objects.requireNonNull(getItem(position)).getCategoryName();
        String type = Objects.requireNonNull(getItem(position)).getType();
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView tvCName = convertView.findViewById(R.id.cm_list_cname);
        TextView tvCType = convertView.findViewById(R.id.cm_list_ctype);
        TextView empty = convertView.findViewById(R.id.empty);
        empty.setText(" ");
        tvCName.setText(name);
        tvCType.setText(type);
        return convertView;
    }
}
