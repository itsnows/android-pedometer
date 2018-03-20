package com.pedometerlibrary.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedometerlibrary.common.PedometerManager;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/20 11:35
 * <p>
 * PedometerBootCompletedReceiver
 */
public class PedometerBootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = PedometerBootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            executeBootTask(context);
            Log.d(TAG, "PedometerBootCompletedReceiver：Intent.ACTION_BOOT_COMPLETED");
        }
    }

    /**
     * 执行开机任务
     */
    private void executeBootTask(Context context) {
        PedometerManager pedometerManager = PedometerManager.newInstance();
        pedometerManager.setApplication(context);
        pedometerManager.setAlarmClock();
        pedometerManager.setJobScheduler();
        pedometerManager.startPedometer();
    }
}
