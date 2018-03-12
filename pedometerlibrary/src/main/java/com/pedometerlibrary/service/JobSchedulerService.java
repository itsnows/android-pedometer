package com.pedometerlibrary.service;

import android.app.Application;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.pedometerlibrary.common.PedometerManager;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/18 11:42
 * <p>
 * 程序调度服务
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    public static final int JOB_REBOOT_PEDOMETER_ID = 0x1201;

    /**
     * 任务处理
     */
    private JobHandler jobHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        jobHandler = new JobHandler();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Message message = Message.obtain(jobHandler, params.getJobId());
        message.obj = params;
        jobHandler.sendMessage(message);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobHandler.removeMessages(params.getJobId());
        return false;
    }

    /**
     * 执行重启计步器
     */
    private void executeRebootPedometer(Application application) {
        PedometerManager.setApplication(application);
        PedometerManager.setJobScheduler();
        PedometerManager.startPedometerService();
    }

    /**
     * 任务处理
     */
    private class JobHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JobParameters jobParameters = (JobParameters) msg.obj;
            switch (msg.what) {
                case JOB_REBOOT_PEDOMETER_ID:
                    executeRebootPedometer((Application) getApplicationContext());
                    jobFinished(jobParameters, false);
                    break;
                default:
                    break;
            }
        }
    }
}
