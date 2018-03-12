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
 * CreatedTime: 2017/12/18 15:11
 * <p>
 * 计步器定时广播
 */

public class PedometerAlarmReceiver extends BroadcastReceiver {
    /**
     * 默认警报
     */
    public static final String ACTION = "com.pedometerlibrary.receive.PedometerShutdownReceiver";
    /**
     * 零点钟警报
     */
    public static final String ACTION_ZERO_CLOCK = "com.pedometerlibrary.receive.PedometerShutdownReceiver.ZERO_CLOCK";
    private static final String TAG = PedometerAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION.equals(action)) {
            Log.v(TAG, "ACTION");
        } else if (ACTION_ZERO_CLOCK.equals(action)) {
            executeZeroClockTask((Application) context.getApplicationContext());
            Log.v(TAG, "ACTION_ZERO_CLOCK");
        }
    }

    /**
     * 执行零点钟警报任务
     */
    private void executeZeroClockTask(Application application) {
        PedometerManager.setApplication(application);
        PedometerManager.setZeroClockAlarm();
        PedometerManager.startPedometerService();
    }

}
