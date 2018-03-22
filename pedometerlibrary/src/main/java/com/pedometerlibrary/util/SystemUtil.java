package com.pedometerlibrary.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/18 0:21
 * <p>
 * SystemUtil
 */

public class SystemUtil {

    private SystemUtil() {
        throw new UnsupportedOperationException("SystemUtil cannot be instantiated");
    }

    /**
     * 是否支持功能传感器-步计数器
     *
     * @param context
     * @return
     */
    public static boolean isSupportStepCounter(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
    }

    /**
     * 是否支持功能传感器-步探测器
     *
     * @param context
     * @return
     */
    public static boolean isSupportStepDetector(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
    }

    /**
     * 是否支持功能传感器-加速计
     *
     * @param context
     * @return
     */
    public static boolean isSupportAccelerometer(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
    }

    /**
     * 获取进程名称
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 进程是否运行
     *
     * @return
     */
    public static boolean isProessRunning(Context context, String proessName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processs = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processs) {
            if (process.processName.equals(proessName)) {
                return true;
            }
        }
        return false;
    }

}
