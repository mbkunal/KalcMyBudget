package com.kmb.budget;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static Date getFirstDate(){
        String cDate = ExportTransactions.formatter.format(new Date());
        Date frDate;
        try {
            frDate = ExportTransactions.formatter.parse("01"+cDate.substring(2));
        } catch (ParseException e) {
            frDate = new Date();
            e.printStackTrace();
        }

        return frDate;
    }
    public static Date getLastDate(){
        String cDate = ExportTransactions.formatter.format(new Date());
        LocalDate convertedDate = LocalDate.parse(cDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        convertedDate = convertedDate.withDayOfMonth(
                convertedDate.getMonth().length(convertedDate.isLeapYear()));
        Date tDate = Date.from(convertedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return tDate;
    }


}
