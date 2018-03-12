package com.pedometerlibrary.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.pedometerlibrary.R;
import com.pedometerlibrary.common.PedometerConstants;
import com.pedometerlibrary.drawable.CircleProgressDrawable;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 22:31
 * <p>
 * SimplePedometerService
 */

public class SimplePedometerService extends BasePedometerService {
    public static final String ACTION = "com.pedometerlibrary.service.SimplePedometerService";
    private static final String TAG = SimplePedometerService.class.getSimpleName();
    private static final int DEFULT_TARGET = 1000;

    /**
     * 通知栏ID
     */
    private static final int NOTIFY_ID = 0x1050;

    /**
     * 通知栏
     */
    private NotificationCompat.Builder builder;
    private NotificationManager notifyManager;

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
        startForeground(NOTIFY_ID, builder.build());
        pedometerServiceReceive = new PedometerServiceReceive();
        pedometerServiceReceive.registerReceiver();
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_pedometer);
        builder = new NotificationCompat.Builder(this);
        startForeground(NOTIFY_ID, builder.build());
        builder.setWhen(System.currentTimeMillis())
                .setContentIntent(getDefaultPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setOngoing(true)
                .setDefaults(Notification.FLAG_AUTO_CANCEL)
                .setSmallIcon(android.R.drawable.sym_def_app_icon);
        builder.setCustomBigContentView(remoteViews);
        builder.setCustomContentView(remoteViews);
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        target = DEFULT_TARGET;
    }

    /**
     * 设置通知栏
     */
    public void setNotify() {
        RemoteViews remoteViews = builder.getContentView();
        remoteViews.setTextViewText(R.id.tv_notification_pedometer_step, getString(R.string.notification_pedometer_step, String.valueOf(step)));
        remoteViews.setTextViewText(R.id.tv_notification_pedometer_description, getString(R.string.notification_pedometer_description));
        CircleProgressDrawable circleProgressDrawable = new CircleProgressDrawable(getResources());
        circleProgressDrawable.setMaxProgress(target);
        circleProgressDrawable.setCurrentProgress(step);
        remoteViews.setImageViewBitmap(R.id.tv_notification_pedometer_target_progress, circleProgressDrawable.getBitmap());
        notifyManager.notify(NOTIFY_ID, builder.build());
    }

    /**
     * 默认通知意图
     */
    private PendingIntent getDefaultPendingIntent(int flag) {
        Intent intent = new Intent(this, SimplePedometerService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, PedometerConstants.DEFAULT_REQUEST_CODE, intent, flag);
        return pendingIntent;
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
    public void onNotSupported() {
        // ...
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        return iPedometerAidlInterface.asBinder();
        return null;
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
            SimplePedometerService.this.registerReceiver(this, filter);
        }

        public void unregisterReceiver() {
            SimplePedometerService.this.unregisterReceiver(this);
        }

    }

}
