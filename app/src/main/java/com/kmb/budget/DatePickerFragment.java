package com.kmb.budget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment {

    private int caller;

    private Date minDate;
    private Date maxDate;
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog;
        if(caller == -123) {
            dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), (MainActivity) getActivity(), year, month, day);
        }else {
            dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), (ExportTransactions) getActivity(), year, month, day);
            if(minDate != null){
                dialog.getDatePicker().setMinDate(minDate.getTime());
            }
            dialog.getDatePicker().setMaxDate(maxDate.getTime());
        }
        return dialog;
    }
    public void setCaller(int caller){
        this.caller = caller;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

}