package com.pedometerlibrary.common;

import android.app.Application;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedometerlibrary.PedometerSDK;
import com.pedometerlibrary.receive.PedometerAlarmReceiver;
import com.pedometerlibrary.service.JobSchedulerService;
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
    private static final String TAG = PedometerSDK.class.getSimpleName();
    public Application application;

    private PedometerManager() {
    }

    public static PedometerManager newInstance() {
        return new PedometerManager();
    }

    public void setApplication(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("context can not be null");
        }
        this.application = application;
    }

    /**
     * 设置记步服务隐式意图
     *
     * @param serviceAction
     */
    public static void setPedometerServiceAction(String serviceAction) {
        PedometerParam.setPedometerServiceAction(application, serviceAction);
    }

    /**
     * 设置系统重新启动状态
     *
     * @param status
     */
    public static void setSystemRebootStatus(boolean status) {
        PedometerParam.setSystemRebootStatus(application, status);
    }

    /**
     * 设置当前App步数
     *
     * @param step
     */
    public static void setCurrentAppStep(int step) {
        PedometerParam.setCurrentAppStep(application, step);
    }

    /**
     * 设置零点警告
     */
    public static void setZeroClockAlarm() {
        Intent intent = new Intent(PedometerAlarmReceiver.ACTION_ZERO_CLOCK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(application, PedometerConstants.DEFAULT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long alarmTime = DateUtil.getDateZeroTime(DateUtil.getTomorrowDate());
        AlarmManagerUtil.setAlarm(application, alarmTime, pendingIntent);
    }

    /**
     * 设置程序调度服务
     */
    public static void setJobScheduler() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            long tomorrowZeroTime = DateUtil.getDateZeroTime(DateUtil.getTomorrowDate());
            long executeTime = tomorrowZeroTime - DateUtil.getSystemTime();
            JobScheduler jobScheduler = (JobScheduler) application.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = null;
            jobInfo = new JobInfo.Builder(
                    JobSchedulerService.JOB_REBOOT_PEDOMETER_ID,
                    new ComponentName(application.getPackageName(), JobSchedulerService.class.getName()))
                    .setMinimumLatency(executeTime)
                    .setOverrideDeadline(executeTime)
                    .setPersisted(false)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                    .setRequiresCharging(true)
                    .setRequiresDeviceIdle(false)
                    .build();
            int request = jobScheduler.schedule(jobInfo);
            if (request > 0) {
                Log.v(TAG, "JobScheduler：JobSchedulerService.JOB_REBOOT_PEDOMETER_ID");
            }
        }
    }

    /**
     * 启动计步器
     */
    public static void startPedometerService() {
        String action = PedometerParam.getPedometerServiceAction(application);
        Intent intent = IntentUtil.createExplicitFromImplicitIntent(application, new Intent(action));
        application.startService(intent);
    }

}
