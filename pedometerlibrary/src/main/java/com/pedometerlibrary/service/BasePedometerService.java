package com.pedometerlibrary.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import com.pedometerlibrary.detector.StepDetectorManager;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 22:28
 * <p>
 * BasePedometerService
 */

public abstract class BasePedometerService extends Service {

    /**
     * 屏幕唤醒锁夜间状态时长
     */
    private static final long WAKE_LOCK_NIGHT_TIME = 5 * 1000L;

    /**
     * 屏幕唤醒锁白天状态时长
     */
    private static final long WAKE_LOCK_DAYTIME_TIME = 5 * 60 * 1000L;

    /**
     * 记步消息标记
     */
    private static final int MSG_STEP = 0x1024;

    /**
     * 暂停记步消息标记
     */
    private static final int MSG_PAUSE_STEP = 0x1025;

    /**
     * 重新记步消息常量
     */
    private static final int MSG_RESUME_STEP = 0x1026;

    /**
     * 不支持记步消息常量
     */
    private static final int MSG_NOT_SUPPORTED = 0x1027;

    /**
     * 步测器步数
     */
    private int step;

    /**
     * 步测器步行状态
     */
    private boolean isPause;

    /**
     * 步测器管理
     */
    private StepDetectorManager stepDetectorManager;

    /**
     * 唤醒屏幕锁
     */
    private PowerManager.WakeLock wakeLock;

    /**
     * 记步任务
     */
    private TaskHandler taskHandler;

    /**
     * 步测器管理回调接口
     */
    private StepDetectorManager.CallBack callBack = new StepDetectorManager.CallBack() {

        @Override
        public void onStep(int step) {
            BasePedometerService.this.step = step;

            taskHandler.sendEmptyMessage(MSG_STEP);

            if (isPause) {
                taskHandler.sendEmptyMessage(MSG_RESUME_STEP);
            }

            if (taskHandler.hasMessages(MSG_PAUSE_STEP)) {
                taskHandler.removeMessages(MSG_PAUSE_STEP);
            }
            taskHandler.sendEmptyMessageDelayed(MSG_PAUSE_STEP, 3000L);
        }

        @Override
        public void onNotSupported() {
            taskHandler.sendEmptyMessage(MSG_NOT_SUPPORTED);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        stepDetectorManager = new StepDetectorManager(this);
        stepDetectorManager.setCallBack(callBack);
        taskHandler = new TaskHandler(this);
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TaskThread.startTask(stepDetectorManager);
        acquireWakeLock(this);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseWakeLock();
    }

    /**
     * 步测器步数
     *
     * @return 步测器当前系统日期步数
     */
    public int getStep() {
        return step;
    }

    /**
     * 步测器步行状态
     *
     * @return true 步行中 false 步行暂停
     */
    public boolean isPause() {
        return isPause;
    }

    /**
     * 获取屏幕锁
     *
     * @param context Context
     */
    private void acquireWakeLock(Context context) {
        releaseWakeLock();
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.setReferenceCounted(true);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 23 || hour <= 7) {
            wakeLock.acquire(WAKE_LOCK_NIGHT_TIME);
        } else {
            wakeLock.acquire(WAKE_LOCK_DAYTIME_TIME);
        }
    }

    /**
     * 释放屏幕锁
     */
    private void releaseWakeLock() {
        if (wakeLock != null) {
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
            wakeLock = null;
        }
    }

    /**
     * 记步
     */
    public abstract void onStep();

    /**
     * 继续记步
     */
    public void onResumeStep() {
    }

    /**
     * 暂停记步
     */
    public void onPauseStep() {
    }

    /**
     * 不支持记步
     */
    public abstract void onNotSupported();

    /**
     * 记步任务
     */
    private static class TaskHandler extends Handler {

        private WeakReference<BasePedometerService> weakReference;

        public TaskHandler(BasePedometerService basePedometerService) {
            this.weakReference = new WeakReference<>(basePedometerService);
        }

        @Override
        public void handleMessage(Message msg) {
            BasePedometerService service = weakReference.get();
            if (service == null) {
                return;
            }
            switch (msg.what) {
                case MSG_STEP:
                    service.onStep();
                    break;
                case MSG_PAUSE_STEP:
                    service.isPause = true;
                    service.onPauseStep();
                    break;
                case MSG_RESUME_STEP:
                    service.isPause = false;
                    service.onResumeStep();
                    break;
                case MSG_NOT_SUPPORTED:
                    service.onNotSupported();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 记步线程
     */
    private static class TaskThread extends Thread {
        private StepDetectorManager stepDetectorManager;

        private TaskThread(StepDetectorManager stepDetectorManager) {
            this.stepDetectorManager = stepDetectorManager;
        }

        public static synchronized void startTask(StepDetectorManager manager) {
            TaskThread taskThread = new TaskThread(manager);
            taskThread.start();
        }

        @Override
        public void run() {
            super.run();
            stepDetectorManager.start();
        }

    }

}
