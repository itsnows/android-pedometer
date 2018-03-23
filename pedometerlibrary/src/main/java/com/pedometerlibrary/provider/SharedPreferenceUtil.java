package com.pedometerlibrary.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;

public class SharedPreferenceUtil {
    public static final String AUTHORITY = "com.msplibrary.SharedPreferenceProvider";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String METHOD_CONTAIN_KEY = "method_contain_key";
    public static final String METHOD_QUERY_VALUE = "method_query_value";
    public static final String METHOD_EIDIT_VALUE = "method_edit";
    public static final String METHOD_QUERY_PID = "method_query_pid";
    public static final String KEY_VALUES = "key_result";
    public static final Uri CONTENT_CREATE = Uri.withAppendedPath(URI, "create");
    public static final Uri CONTENT_CHANGED = Uri.withAppendedPath(URI, "changed");

    public static SharedPreferences getSharedPreference(@NonNull Context context, String name) {
        return SharedPreferenceProxy.getSharedPreferences(context, name);
    }
}
