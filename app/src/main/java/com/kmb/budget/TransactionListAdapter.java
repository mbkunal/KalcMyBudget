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

class TransactionListAdapter extends ArrayAdapter<Transaction> {


    private Context context;
    private int resource;



    public TransactionListAdapter(@NonNull Context context, int resource, @NonNull List<Transaction> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String sr = getItem(position).getSr();
        String from = getItem(position).getFrom();
        String to = getItem(position).getTo();
        String comment = getItem(position).getComment();
        String date = getItem(position).getDate();
        String amount = getItem(position).getAmount();

        Transaction transaction = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);
        TextView tvSr = convertView.findViewById(R.id.tr_list_sr);
        TextView tvTo = convertView.findViewById(R.id.tr_list_to);
        TextView tvFrom = convertView.findViewById(R.id.tr_list_from);
        TextView tvComment = convertView.findViewById(R.id.tr_list_comment);
        TextView tvDate = convertView.findViewById(R.id.tr_list_td);
        TextView tvAmount = convertView.findViewById(R.id.tr_list_amount);
        tvSr.setText(sr);
        tvTo.setText(to);
        tvFrom.setText(from);
        tvComment.setText(comment);
        tvAmount.setText(amount);
        tvDate.setText(date);

        return convertView;
    }
}