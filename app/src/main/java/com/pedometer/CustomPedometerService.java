package com.pedometer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pedometerlibrary.R;
import com.pedometerlibrary.common.PedometerConstants;
import com.pedometerlibrary.service.BasePedometerService;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 22:31
 * <p>
 * CustomPedometerService
 */
@SuppressLint("WrongConstant")
public class CustomPedometerService extends BasePedometerService {
    public static final String ACTION = "com.pedometer.CustomPedometerService";
    private static final String TAG = CustomPedometerService.class.getSimpleName();
    private static final int DEFULT_TARGET = 1000;

    /**
     * 通知栏ID
     */
    private static final int NOTIFY_ID = 0x1050;

    /**
     * 通知栏
     */
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    /**
     * 目标
     */
    private int target;

    /**
     * 计步器服务广播
     */
    private PedometerServiceReceive pedometerServiceReceive;

    /**
     * 计步器进程通信接口
     */
//    private IPedometerAidlInterface.Stub iPedometerAidlInterface = new IPedometerAidlInterface.Stub() {
//        @Override
//        public int onStep() throws RemoteException {
//            return PedometerStep;
//        }
//
//        @Override
//        public void setTarget(int value) throws RemoteException {
//            target = value;
//            setNotify();
//        }
//    };
    @Override
    public void onCreate() {
        super.onCreate();
        initNotify();

        if (pedometerServiceReceive == null) {
            pedometerServiceReceive = new PedometerServiceReceive();
            pedometerServiceReceive.registerReceiver();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        pedometerServiceReceive.unregisterReceiver();
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public void onStep() {
        setNotify();
    }

    @Override
    public void onResumeStep() {
        super.onResumeStep();
    }

    @Override
    public void onPauseStep() {
        super.onPauseStep();
    }

    @Override
    public void onNotSupported() {
        
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(NOTIFY_ID), getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setShowBadge(true);
            channel.setSound(null, null);
            channel.setVibrationPattern(null);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, String.valueOf(NOTIFY_ID));
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        builder.setWhen(System.currentTimeMillis())
                .setContentIntent(getDefaultPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setOngoing(true)
                .setDefaults(Notification.FLAG_AUTO_CANCEL)
                .setSmallIcon(android.R.drawable.sym_def_app_icon);
        startForeground(NOTIFY_ID, builder.build());
        target = DEFULT_TARGET;
    }

    /**
     * 设置通知栏
     */
    @SuppressLint("RestrictedApi")
    public void setNotify() {
        builder.setContentTitle(String.valueOf(getStep()) + " 步");
        builder.setContentText("今日步数");
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    /**
     * 默认通知意图
     */
    private PendingIntent getDefaultPendingIntent() {
        return PendingIntent.getService(this,
                PedometerConstants.DEFAULT_REQUEST_CODE,
                new Intent(this, CustomPedometerService.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 计步器服务广播
     */
    private class PedometerServiceReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.v(TAG, "SCREEN ON");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.v(TAG, "SCREEN OFF");
            } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {
                Log.v(TAG, "Intent.ACTION_DATE_CHANGED");
            } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                Log.v(TAG, "Intent.ACTION_TIME_CHANGED");
            } else if (Intent.ACTION_TIME_TICK.equals(action)) {
                Log.v(TAG, "Intent.ACTION_TIME_TICK");
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                Log.v(TAG, "Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
            } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
                Log.v(TAG, "Intent.ACTION_SHUTDOWN");
            }
        }

        public void registerReceiver() {
            IntentFilter filter = new IntentFilter();
            // 屏幕开启广播
            filter.addAction(Intent.ACTION_SCREEN_ON);
            // 屏幕关闭广播
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            // 日期变化广播
            filter.addAction(Intent.ACTION_DATE_CHANGED);
            // 时间变化广播
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            // 1分钟/次广播
            filter.addAction(Intent.ACTION_TIME_TICK);
            // 关机对话框弹出广播
            filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            // 关机广播
            filter.addAction(Intent.ACTION_SHUTDOWN);
            CustomPedometerService.this.registerReceiver(this, filter);
        }

        public void unregisterReceiver() {
            CustomPedometerService.this.unregisterReceiver(this);
        }

    }

}
