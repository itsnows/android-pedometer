package com.pedometerlibrary.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedometerlibrary.common.PedometerManager;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/20 11:45
 * <p>
 * PedometerAlarmReceiver
 */
public class PedometerAlarmReceiver extends BroadcastReceiver {
    /**
     * 零点闹钟
     */
    public static final String ACTION_ZERO_ALARM_CLOCK = "com.pedometerlibrary.receive.PedometerActionReceiver.ACTION_ZERO_ALARM_CLOCK";
    /**
     * 零点工作
     */
    public static final String ACTION_ZERO_JOB_SCHEDULER = "com.pedometerlibrary.receive.PedometerActionReceiver.ACTION_ZERO_JOB_SCHEDULER";
    private static final String TAG = PedometerAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_ZERO_ALARM_CLOCK.equals(action)) {
            executeZeroClockTask();
            Log.d(TAG, "PedometerAlarmReceiver：ACTION_ZERO_ALARM_CLOCK");
        } else if (ACTION_ZERO_JOB_SCHEDULER.equals(action)) {
            executeRebootPedometerTask();
            Log.d(TAG, "PedometerAlarmReceiver：ACTION_ZERO_JOB_SCHEDULER");
        }
    }

    /**
     * 执行零点钟闹钟任务
     */
    private void executeZeroClockTask() {
        PedometerManager pedometerManager = PedometerManager.getInstance();
        pedometerManager.setAlarmClock();
        pedometerManager.start();
    }

    /**
     * 执行零点工作任务
     */
    private void executeRebootPedometerTask() {
        PedometerManager pedometerManager = PedometerManager.getInstance();
        pedometerManager.setJobScheduler();
        pedometerManager.start();
    }
}
