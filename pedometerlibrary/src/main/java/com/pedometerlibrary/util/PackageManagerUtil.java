package com.pedometerlibrary.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/1/2 11:31
 * <p>
 * PackageManagerUtil
 */

public class PackageManagerUtil {
    private PackageManagerUtil() {
        throw new UnsupportedOperationException("PackageManagerUtil cannot be instantiated");
    }

    /**
     * 获取应用程序图标
     */
    public static Drawable getAppIcon(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return applicationInfo.loadIcon(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return context.getResources().getString(packageInfo.applicationInfo.labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取元素值
     *
     * @param key
     * @return
     */
    public static Object getMetaData(Context context, String key) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return applicationInfo.metaData.get(key);
    }

    /**
     * 获取应用版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int code = -1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

}
