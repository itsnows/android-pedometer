package com.pedometerlibrary.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/19 11:32
 * <p>
 * AlarmManagerUtil
 */

public class AlarmManagerUtil {

    private AlarmManagerUtil() {
        throw new UnsupportedOperationException(
                "AlarmManagerUtil cannot be instantiated");
    }

    /**
     * 设置警报
     *
     * @param context
     * @param time          警报时间
     * @param pendingIntent 警报意图
     */
    public static void setAlarm(Context context, long time, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(time, pendingIntent);
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

}
