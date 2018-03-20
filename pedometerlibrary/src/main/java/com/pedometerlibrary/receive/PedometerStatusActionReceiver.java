package com.pedometerlibrary.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/13 15:39
 * <p>
 * 计步器状态活动广播
 */
public abstract class PedometerStatusActionReceiver extends BroadcastReceiver {
    public static final String TAG = PedometerStatusActionReceiver.class.getSimpleName();
    public static final String ACTION = "com.pedometerlibrary.receive.PedometerStatusActionReceiver";

    @Override
    public final void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION.equals(action)) {
            onAction(context);
        }
        Log.d(TAG, action);
    }

    /**
     * 记步状态通知栏意图
     *
     * @param context Context
     */
    public abstract void onAction(Context context);

    /**
     * 注册广播
     *
     * @param context Context
     */
    public void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        context.registerReceiver(this, intentFilter);
    }

    /**
     * 移除广播
     *
     * @param context Context
     */
    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(this);
    }

}
