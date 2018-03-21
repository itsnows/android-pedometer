package com.pedometerlibrary.common;

import android.app.Application;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedometerlibrary.Pedometer;
import com.pedometerlibrary.receive.PedometerAlarmReceiver;
import com.pedometerlibrary.service.PedometerJobSchedulerService;
import com.pedometerlibrary.service.PedometerService;
import com.pedometerlibrary.util.AlarmManagerUtil;
import com.pedometerlibrary.util.DateUtil;
import com.pedometerlibrary.util.IntentUtil;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/27 12:32
 * <p>
 * PedometerManager
 */

public class PedometerManager {
    private static final String TAG = Pedometer.class.getSimpleName();
    private static Context context;
    private static PedometerManager manager;
    private static PedometerOptions options;
    private static boolean isInitialized;

    /**
     * 获取计步器管理实例
     *
     * @return PedometerManager
     */
    public static synchronized PedometerManager getInstance() {
        if (manager == null) {
            manager = new PedometerManager();
        }
        return manager;
    }

    /**
     * 是否初始化计步器
     *
     * @return
     */
    public static boolean isIsInitialized() {
        return isInitialized;
    }

    /**
     * 获取计步器参数
     *
     * @return
     */
    public static PedometerOptions getOptions() {
        return options;
    }

    public PedometerManager() {
    }

    public void initialize(Context context, PedometerOptions options) {
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }

        if (!(context instanceof Application)) {
            throw new RuntimeException("Context must be an application context");
        }

        if (options == null) {
            throw new RuntimeException("PedometerOptions can not be null");
        }

        if (!isInitialized) {
            isInitialized = true;
            PedometerManager.context = context.getApplicationContext();
            PedometerManager.options = options;
        }

        this.initialize(context, PedometerService.ACTION);
    }

    public void initialize(Context context, String action) {
        if (context == null) {
            throw new IllegalArgumentException("Context can not be null");
        }

        if (!(context instanceof Application)) {
            throw new RuntimeException("Context must be an application context");
        }

        if (action == null) {
            throw new RuntimeException("PedometerAction can not be null");
        }

        if (!isInitialized) {
            isInitialized = true;
            PedometerManager.context = context.getApplicationContext();
            setAction(action);
        }
    }

    /**
     * 绑定记步服务隐式意图
     *
     * @param action 计步器活动隐式意图
     */
    public void setAction(String action) {
        PedometerParam.setPedometerAction(context, action);
    }

    /**
     * 设置零点警告
     */
    public void setAlarmClock() {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                PedometerConstants.DEFAULT_REQUEST_CODE,
                new Intent(PedometerAlarmReceiver.ACTION_ZERO_ALARM_CLOCK),
                PendingIntent.FLAG_UPDATE_CURRENT);
        long alarmTime = DateUtil.getDateZeroTime(DateUtil.getTomorrowDate());
        AlarmManagerUtil.setAlarm(context, alarmTime, pendingIntent);
    }

    /**
     * 设置程序调度服务
     */
    public void setJobScheduler() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            long tomorrowZeroTime = DateUtil.getDateZeroTime(DateUtil.getTomorrowDate());
            long executeTime = tomorrowZeroTime - DateUtil.getSystemTime();
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                ComponentName componentName = new ComponentName(context.getPackageName(), PedometerJobSchedulerService.class.getName());
                JobInfo jobInfo = new JobInfo.Builder(PedometerJobSchedulerService.JOB_REBOOT_PEDOMETER_ID, componentName)
                        .setMinimumLatency(executeTime)
                        .setOverrideDeadline(executeTime)
                        .setPersisted(false)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                        .setRequiresCharging(true)
                        .setRequiresDeviceIdle(false)
                        .build();
                int request = jobScheduler.schedule(jobInfo);
                if (request > 0) {
                    Log.v(TAG, "JobScheduler：PedometerJobSchedulerService.JOB_REBOOT_PEDOMETER_ID");
                    return;
                }
            }
        }
        Log.v(TAG, "JobScheduler：Not supported");
    }

    /**
     * 启动计步器
     */
    public void start() {
        context.startService(IntentUtil.createExplicitFromImplicitIntent(context, new Intent(PedometerParam.getPedometerAction(context))));
    }

}
