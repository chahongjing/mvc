package com.zjy.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    public static String getTimeFromLong(long millisecond) {
        return getTimeFromLong(millisecond, "HH:mm:ss");
    }
    public static String getTimeFromLong(long millisecond, String format) {
        if(millisecond > 0) {
            Date date = new Date(millisecond);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            return sdf.format(date);
        }
        return "";
    }
}
