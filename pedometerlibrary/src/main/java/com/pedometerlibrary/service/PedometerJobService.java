package com.pedometerlibrary.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.pedometerlibrary.common.PedometerManager;
import com.pedometerlibrary.receive.PedometerAlarmReceiver;

import java.lang.ref.SoftReference;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/18 11:42
 * <p>
 * 程序调度服务
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PedometerJobService extends JobService {
    private static final String TAG = PedometerJobService.class.getSimpleName();
    public static final int JOB_REBOOT_ID = 0x1201;
    public static final int JOB_MIDNIGHT_ID = 0x1520;
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
        Message message = Message.obtain();
        message.what = params.getJobId();
        message.obj = params;
        jobHandler.sendMessage(message);
        Log.d(TAG, "onStartJob");
        if (!PedometerManager.getInstance().isRunning()) {
            PedometerManager.getInstance().start();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob");
        return false;
    }

    /**
     * 任务处理
     */
    private static class JobHandler extends Handler {
        private SoftReference<PedometerJobService> weakReference;

        private JobHandler(PedometerJobService service) {
            this.weakReference = new SoftReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            PedometerJobService service = weakReference.get();
            if (service == null) {
                return;
            }
            JobParameters jobParameters = (JobParameters) msg.obj;
            service.jobFinished(jobParameters, false);
            switch (msg.what) {
                case JOB_REBOOT_ID:
                    PedometerManager.getInstance().setRebootJobScheduler();
                    if (!PedometerManager.getInstance().isRunning()) {
                        PedometerManager.getInstance().start();
                    }
                    break;
                case JOB_MIDNIGHT_ID:
                    service.sendBroadcast(new Intent(PedometerAlarmReceiver.ACTION_MIDNIGHT_JOB_SCHEDULER));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
