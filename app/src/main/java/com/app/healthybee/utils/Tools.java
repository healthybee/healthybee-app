package com.app.healthybee.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tools {
    public static Locale locale = Locale.US;
    public static long timeStringtoMilis(String time) {
        long milis = 0;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sd.parse(time);
            milis = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return milis;
    }

    public static String getFormatedDate(String date_str) {
        if (date_str != null && !date_str.trim().equals("")) {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm");
            try {
                String newStr = newFormat.format(oldFormat.parse(date_str));
                return newStr;
            } catch (ParseException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getFormatedDateSimple(String date_str) {
        if (date_str != null && !date_str.trim().equals("")) {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
            try {
                String newStr = newFormat.format(oldFormat.parse(date_str));
                return newStr;
            } catch (ParseException e) {
                return "";
            }
        } else {
            return "";
        }
    }
    public static String formatDateForDisplay(Date d, String format) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(format, getAppLocale()).format(d);

    }

    private static Locale getAppLocale() {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }
    public static String convertDateyyyymmddToddmmyyyy(String yyyymmdd) {
        String ddmmyyyy;
        Locale locale = new Locale("en", "UK");
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MMM-dd",locale);
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy",locale);
        Date date = null;
        try {
            date = inputFormat.parse(yyyymmdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ddmmyyyy = outputFormat.format(date);
        return ddmmyyyy;
    }
}
