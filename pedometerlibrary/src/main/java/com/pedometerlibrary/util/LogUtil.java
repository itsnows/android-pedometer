package com.pedometerlibrary.util;

import android.util.Log;

public final class LogUtil {

    private final static String TAG = LogUtil.class.getSimpleName();

    /**
     * 开发版：1-5
     * 发布版：0
     */
    private final static int LEVEL = 5;

    private LogUtil() {
        throw new UnsupportedOperationException("LogUtil cannot instantiated!");
    }

    /**
     * VERBOSE
     *
     * @param tag TAG
     * @param msg MESSAGE
     */
    public static void v(String tag, String msg) {
        if (LEVEL >= 5)
            Log.v(tag == null ? TAG : tag, msg == null ? "" : msg);
    }

    /**
     * DEBUG
     *
     * @param tag TAG
     * @param msg MESSAGE
     */
    public static void d(String tag, String msg) {
        if (LEVEL >= 4)
            Log.d(tag == null ? TAG : tag, msg == null ? "" : msg);
    }

    /**
     * INFO
     *
     * @param tag TAG
     * @param msg MESSAGE
     */
    public static void i(String tag, String msg) {
        if (LEVEL >= 3)
            Log.i(tag == null ? TAG : tag, msg == null ? "" : msg);
    }

    /**
     * WARN
     *
     * @param tag TAG
     * @param msg MESSAGE
     */
    public static void w(String tag, String msg) {
        if (LEVEL >= 2)
            Log.w(tag == null ? TAG : tag, msg == null ? "" : msg);
    }

    /**
     * ERROR
     *
     * @param tag TAG
     * @param msg MESSAGE
     */
    public static void e(String tag, String msg) {
        if (LEVEL >= 1)
            Log.e(tag == null ? TAG : tag, msg == null ? "" : msg);
    }

}
