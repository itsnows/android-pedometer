package com.pedometerlibrary.receive;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedometerlibrary.common.PedometerManager;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 22:06
 * <p>
 * 计步器开机广播
 */

public class PedometerBootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = PedometerBootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            executeBootTask((Application) context.getApplicationContext());
            Log.v(TAG, "Intent.ACTION_BOOT_COMPLETED");
        }
    }

    /**
     * 执行开机任务
     */
    private void executeBootTask(Application application) {
        PedometerManager.setApplication(application);
        PedometerManager.setSystemRebootStatus(true);
        PedometerManager.setZeroClockAlarm();
        PedometerManager.setJobScheduler();
        PedometerManager.startPedometerService();
    }

}
