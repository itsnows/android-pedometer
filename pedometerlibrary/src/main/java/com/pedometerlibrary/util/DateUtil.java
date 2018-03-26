package com.pedometerlibrary.util;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 22:44
 * <p>
 * DateUtil
 */

public class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException(
                "DateUtil cannot be instantiated");
    }

    /**
     * 判断时间是否是午夜12点钟
     *
     * @param time
     * @return
     */
    public static boolean isMidnightTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour == 0 && minute == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定日期的午夜12点钟时间
     *
     * @param date
     * @return
     */
    public static long getDateMidnightTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取系统开机时间
     *
     * @return
     */
    public static long getSystemBootTime() {
        return getSystemTime() - SystemClock.elapsedRealtime();
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static long getSystemTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return parseDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取明天日期
     *
     * @return
     */
    public static Date getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, +1);
        return calendar.getTime();
    }

    /**
     * 两个时间相差天数
     *
     * @param time0
     * @param time1
     * @return
     */
    public static int differentDays(long time0, long time1) {
        Calendar calendar0 = Calendar.getInstance();
        calendar0.setTimeInMillis(time0);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        int day0 = calendar0.get(Calendar.DAY_OF_YEAR);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);

        int year0 = calendar0.get(Calendar.YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        if (year0 != year1) {
            int timeDistance = 0;
            for (int i = year0; i < year1; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + Math.abs((day1 - day0));
        } else {
            return Math.abs(day1 - day0);
        }
    }

    /**
     * 解析日期
     *
     * @param date
     * @param pattern
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static Date parseDate(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析日期
     *
     * @param date
     * @param pattern
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String parseDate(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

}
