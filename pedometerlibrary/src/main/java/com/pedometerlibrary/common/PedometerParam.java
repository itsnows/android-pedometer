package com.pedometerlibrary.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/18 0:06
 * <p>
 * 计步器参数
 */

public class PedometerParam {
    private static final String FILE_NAME = "pedometer";
    private static final String KEY_NAME_CURRENT_APP_STEP = "current_app_step";
    private static final String KEY_NAME_LAST_SENSOR_STEP = "last_sensor_step";
    private static final String KEY_NAME_LAST_SENSOR_TIME = "last_sensor_time";
    private static final String KEY_NAME_LAST_OFFSET_STEP = "last_offset_step";
    private static final String KEY_NAME_SYSTEM_BOOT_TIME = "system_boot_time";
    private static final String KEY_NAME_SYSTEM_REBOOT_STATUS = "system_reboot_status";
    private static final String KEY_NAME_PEDOMETER_SERVICE_ACTION = "pedometer_service_action";

    /**
     * 当前App步数
     */
    public static int getCurrentAppStep(Context context) {
        return (int) get(context, KEY_NAME_CURRENT_APP_STEP, 0);
    }

    public static void setCurrentAppStep(Context context, int value) {
        put(context, KEY_NAME_CURRENT_APP_STEP, value);
    }

    /**
     * 最后一次传感器步数
     */
    public static int getLastSensorStep(Context context) {
        return (int) get(context, KEY_NAME_LAST_SENSOR_STEP, 0);
    }

    public static void setLastSensorStep(Context context, int value) {
        put(context, KEY_NAME_LAST_SENSOR_STEP, value);
    }

    /**
     * 最后一次传感器时间
     */
    public static long getLastSensorTime(Context context) {
        return (long) get(context, KEY_NAME_LAST_SENSOR_TIME, -28800000L);
    }

    public static void setLastSensorTime(Context context, long time) {
        put(context, KEY_NAME_LAST_SENSOR_TIME, time);
    }

    /**
     * 最后一次步数偏移量
     */
    public static int getLastOffsetStep(Context context) {
        return (int) get(context, KEY_NAME_LAST_OFFSET_STEP, 0);
    }

    public static void setLastOffsetStep(Context context, int value) {
        put(context, KEY_NAME_LAST_OFFSET_STEP, value);
    }

    /**
     * 系统开机时间
     */
    public static long getSystemBootTime(Context context) {
        return (long) get(context, KEY_NAME_SYSTEM_BOOT_TIME, -28800000L);
    }

    public static void setSystemBootTime(Context context, long value) {
        put(context, KEY_NAME_SYSTEM_BOOT_TIME, value);
    }

    /**
     * 系统重新启动状态
     */
    public static boolean getSystemRebootStatus(Context context) {
        return (boolean) get(context, KEY_NAME_SYSTEM_REBOOT_STATUS, false);
    }

    public static void setSystemRebootStatus(Context context, boolean status) {
        put(context, KEY_NAME_SYSTEM_REBOOT_STATUS, status);
    }

    /**
     * 计步器服务ACTION
     */
    public static String getPedometerServiceAction(Context context) {
        return (String) get(context, KEY_NAME_PEDOMETER_SERVICE_ACTION, "com.pedometerlibrary.service.PedometerService");
    }

    public static void setPedometerServiceAction(Context context, String pedometerAction) {
        put(context, KEY_NAME_PEDOMETER_SERVICE_ACTION, pedometerAction);
    }

    /**
     * 保存参数
     *
     * @param context
     * @param key
     * @param value
     */
    private static void put(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.apply();
    }

    /**
     * 获取参数
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    private static Object get(Context context, String key, Object defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultValue instanceof String) {
            return sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultValue);
        }
        return null;
    }
}
