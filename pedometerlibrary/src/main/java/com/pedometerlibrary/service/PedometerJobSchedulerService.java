package com.pedometerlibrary.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.pedometerlibrary.receive.PedometerAlarmReceiver;

import java.lang.ref.WeakReference;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/18 11:42
 * <p>
 * 程序调度服务
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PedometerJobSchedulerService extends JobService {
    public static final int JOB_REBOOT_PEDOMETER_ID = 0x1201;
    private static final String TAG = PedometerJobSchedulerService.class.getSimpleName();
    /**
     * 任务处理
     */
    private JobHandler jobHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        jobHandler = new JobHandler(this);
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Message message = Message.obtain(jobHandler, params.getJobId());
        message.obj = params;
        jobHandler.sendMessage(message);
        Log.d(TAG, "onStartJob");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobHandler.removeMessages(params.getJobId());
        Log.d(TAG, "onStopJob");
        return false;
    }

    /**
     * 任务处理
     */
    private static class JobHandler extends Handler {
        private WeakReference<PedometerJobSchedulerService> weakReference;

        private JobHandler(PedometerJobSchedulerService pedometerJobSchedulerService) {
            this.weakReference = new WeakReference<>(pedometerJobSchedulerService);
        }

        @Override
        public void handleMessage(Message msg) {
            PedometerJobSchedulerService service = weakReference.get();
            if (service == null) {
                return;
            }
            JobParameters jobParameters = (JobParameters) msg.obj;
            switch (msg.what) {
                case JOB_REBOOT_PEDOMETER_ID:
                    service.sendBroadcast(new Intent(PedometerAlarmReceiver.ACTION_ZERO_JOB_SCHEDULER));
                    service.jobFinished(jobParameters, false);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
