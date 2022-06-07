package com.meta.utils;

import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

@Log4j2
public class TimeZoneFormatUtil {

    public static String format(Timestamp timestamp){
        log.info("进行时区转换, timeZone: UTC");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy hh:mm a", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(timestamp.getTime());
    }

    public static String format(Timestamp timestamp, Integer timeZone){
        log.info("进行时区转换, timeZone:{}", timeZone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy hh:mm a", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.HOUR, timeZone);
        return simpleDateFormat.format(calendar.getTime());
    }
}
