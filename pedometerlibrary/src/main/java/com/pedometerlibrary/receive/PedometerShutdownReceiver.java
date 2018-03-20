package com.pedometerlibrary.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedometerlibrary.common.PedometerManager;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/20 11:33
 * <p>
 * PedometerShutdownReceiver
 */
public class PedometerShutdownReceiver extends BroadcastReceiver {
    private static final String TAG = PedometerShutdownReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SHUTDOWN.equals(action)) {
            executeShutdownTask(context);
            Log.d(TAG, "PedometerShutdownReceiver：Intent.ACTION_SHUTDOWN");
        }
    }

    /**
     * 执行关机任务
     */
    private void executeShutdownTask(Context context) {
        PedometerManager pedometerManager = PedometerManager.newInstance();
        pedometerManager.setApplication(context);
        pedometerManager.setAlarmClock();
        pedometerManager.setJobScheduler();
        pedometerManager.startPedometer();
    }

}
