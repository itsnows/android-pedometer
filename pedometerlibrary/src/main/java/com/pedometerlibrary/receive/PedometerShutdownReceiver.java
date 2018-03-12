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
 * CreatedTime: 2017/12/17 22:19
 * <p>
 * 计步器关机广播
 */

public class PedometerShutdownReceiver extends BroadcastReceiver {
    private static final String TAG = PedometerShutdownReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SHUTDOWN.equals(action)) {
            executeShutdownTask((Application) context.getApplicationContext());
            Log.v(TAG, "Intent.ACTION_SHUTDOWN");
        }
    }

    /**
     * 执行关机任务
     */
    private void executeShutdownTask(Application application) {
        PedometerManager.setApplication(application);
        PedometerManager.setZeroClockAlarm();
        PedometerManager.setJobScheduler();
        PedometerManager.startPedometerService();
    }
}
