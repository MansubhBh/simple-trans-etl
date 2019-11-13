package com.mansubh.util;

import com.mansubh.exception.MyParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private DateUtil() {
    }

    public static Date parseDate(String inputdate) {
        return parseDate(inputdate, "yyyy-MM-dd");
    }


    public static Date parseDate(String inputdate, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(inputdate);
        } catch (ParseException e) {
            throw new MyParseException("exception while date parsing ",e);
        }
    }
}
