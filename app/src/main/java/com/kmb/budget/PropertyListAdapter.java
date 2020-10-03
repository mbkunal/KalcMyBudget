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

class PropertyListAdapter extends ArrayAdapter<PropertyModal> {

    private Context context;
    private int resource;



    public PropertyListAdapter(@NonNull Context context, int resource, @NonNull List<PropertyModal> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String type = Objects.requireNonNull(getItem(position)).getType();
        String name = Objects.requireNonNull(getItem(position)).getPropName();
        long value = Objects.requireNonNull(getItem(position)).getPropValue();

        PropertyModal propertyModal = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView tVName = convertView.findViewById(R.id.property_list_name);
        TextView tVType = convertView.findViewById(R.id.property_list_type);
        TextView tVValue = convertView.findViewById(R.id.property_list_value);
        tVName.setText(name);
        tVType.setText(type);
        tVValue.setText(String.valueOf(value));

        return convertView;
    }
}
