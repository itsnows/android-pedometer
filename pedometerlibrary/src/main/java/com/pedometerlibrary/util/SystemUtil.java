package com.pedometerlibrary.util;

import android.content.Context;
import android.content.pm.PackageManager;

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

}
