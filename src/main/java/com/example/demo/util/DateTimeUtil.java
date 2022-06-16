package com.example.demo.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public enum DateTimeFormatterPattern {
        /** yyyyMMddHHmmssSSS */
        PATTERN1("yyyyMMddHHmmssSSS"),
        /** yyyy-MM-dd'T'HH:mm:ssXXX */
        PATTERN2("yyyy-MM-dd'T'HH:mm:ssXXX"),
        /** yyyyMMddHHmmss */
        PATTERN3("yyyyMMddHHmmss"),
        /** yyyy-MM-dd'T'HH:mm:ss.SSSXXX */
        PATTERN4("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
        /** yyyy-MM-dd */
        PATTERN5("yyyy-MM-dd"),
        /** yyyyMMdd */
        PATTERN6("yyyyMMdd"),
        /** yyyy/MM/dd */
        PATTERN7("yyyy/MM/dd"),
        /** yyyy-MM-dd'T'HH:mm:ss */
        PATTERN8("yyyy-MM-dd'T'HH:mm:ss"),
        /** MMyy */
        PATTERN9("MMyy"),
        /** dd/MM/yyyy HH:mm:ss */
        PATTERN10("dd/MM/yyyy HH:mm:ss"),
        /** yyyy-MM-dd HH:mm */
        PATTERN11("yyyy-MM-dd HH:mm"),
        /** yyyy-MM-dd HH:mm:ss */
        PATTERN12("yyyy-MM-dd HH:mm:ss"),
        /** yyyy/MM/dd HH:mm:ss */
        PATTERN13("yyyy/MM/dd HH:mm:ss"),
        /** yyMMddHHmmssSSS */
        PATTERN14("yyMMddHHmmssSSS"),
        ;

        private String value;

        DateTimeFormatterPattern(String value) {
            this.value = value;
        }

        public final String getValue() {
            return value;
        }
    }

    public static String getCurrentFormatedByPattern(DateTimeFormatterPattern pattern) {
        return new SimpleDateFormat(pattern.getValue()).format(getCurrentTime());
    }

    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static boolean isTimeAfterCurrent(Date time) {
        return isTime1AfterTime2(time, getCurrentTime());
    }

    public static boolean isTimeBeforeCurrent(Date time) {
        return isTime1BeforeTime2(time, getCurrentTime());
    }

    public static boolean isTime1AfterTime2(Date time1, Date time2) {
        Calendar t1 = Calendar.getInstance();
        t1.setTime(time1);
        Calendar t2 = Calendar.getInstance();
        t2.setTime(time2);
        return t1.after(t2);
    }

    public static boolean isTime1BeforeTime2(Date time1, Date time2) {
        Calendar t1 = Calendar.getInstance();
        t1.setTime(time1);
        Calendar t2 = Calendar.getInstance();
        t2.setTime(time2);
        return t1.before(t2);
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentTime());
        calendar.add(Calendar.SECOND, 100);
        System.out.println(isTimeBeforeCurrent(calendar.getTime()));
        System.out.println(getCurrentFormatedByPattern(DateTimeFormatterPattern.PATTERN14));
    }
}
