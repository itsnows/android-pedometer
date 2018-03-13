package com.pedometerlibrary.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedometerlibrary.common.PedometerManager;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/13 9:25
 * <p>
 * PedometerActionReceiver
 */
public class PedometerActionReceiver extends BroadcastReceiver {
    private static final String TAG = PedometerActionReceiver.class.getSimpleName();
    /**
     * 零点闹钟
     */
    public static final String ACTION_ZERO_ALARM_CLOCK = "com.pedometerlibrary.receive.PedometerActionReceiver.ACTION_ZERO_ALARM_CLOCK";

    /**
     * 零点工作
     */
    public static final String ACTION_ZERO_JOB_SCHEDULER = "com.pedometerlibrary.receive.PedometerActionReceiver.ACTION_ZERO_JOB_SCHEDULER";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            executeBootTask(context.getApplicationContext());
            Log.v(TAG, "ACTION_BOOT_COMPLETE");
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
            executeShutdownTask(context.getApplicationContext());
            Log.v(TAG, "ACTION_SHUTDOWN");
        } else if (ACTION_ZERO_ALARM_CLOCK.equals(action)) {
            executeZeroClockTask(context.getApplicationContext());
            Log.v(TAG, "ACTION_ZERO_ALARM_CLOCK");
        } else if (ACTION_ZERO_JOB_SCHEDULER.equals(action)) {
            executeRebootPedometer(context.getApplicationContext());
            Log.v(TAG, "ACTION_ZERO_JOB_SCHEDULER");
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

    /**
     * 执行零点钟闹钟任务
     */
    private void executeZeroClockTask(Context context) {
        PedometerManager pedometerManager = PedometerManager.newInstance();
        pedometerManager.setApplication(context);
        pedometerManager.setAlarmClock();
        pedometerManager.startPedometer();
    }

    /**
     * 执行零点工作任务
     */
    private void executeRebootPedometer(Context context) {
        PedometerManager pedometerManager = PedometerManager.newInstance();
        pedometerManager.setApplication(context);
        pedometerManager.setJobScheduler();
        pedometerManager.startPedometer();
    }

}
