package com.example.roomw.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    // Formato usado para created_at
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String now() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date());
    }
}
