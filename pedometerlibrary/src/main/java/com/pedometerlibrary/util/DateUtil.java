package com.pedometerlibrary.util;

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
     * 判断时间是否是零点钟
     *
     * @param date
     * @return
     */
    public static boolean isZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour == 0 && minute == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定日期的零点时间
     *
     * @param date
     * @return
     */
    public static long getDateZeroTime(Date date) {
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
        return dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
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
     * 日期date1和日期date2相差天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + Math.abs((day2 - day1));
        } else {
            return Math.abs(day2 - day1);
        }
    }

    /**
     * 解析时间字符串
     *
     * @param date
     * @param pattern
     * @return
     */
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
     * 日期转化字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

}
