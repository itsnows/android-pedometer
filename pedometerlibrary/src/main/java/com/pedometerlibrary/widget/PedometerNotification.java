package com.pedometerlibrary.widget;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.pedometerlibrary.util.PackageManagerUtil;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/16 9:45
 * <p>
 * PedometerNotification
 */
public class PedometerNotification {
    private Context context;
    private int id;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    public PedometerNotification(Context context, int id) {
        this.context = context;
        this.id = id;
        init();
    }

    private void init() {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(id), PackageManagerUtil.getAppName(context), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setShowBadge(true);
            channel.setSound(null, null);
            channel.setVibrationPattern(null);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, String.valueOf(id));
        } else {
            builder = new NotificationCompat.Builder(context);
        }
    }

    /**
     * @return Context
     */
    public Context getContext() {
        return context;
    }

    /**
     * 获取通知栏构建者
     *
     * @return NotificationCompat.Builder
     */
    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    /**
     * 获取通知栏
     *
     * @return Notification
     */
    public Notification getNotification() {
        return builder.build();
    }

    /**
     * 获取通知栏管理
     *
     * @return NotificationManager
     */
    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    /**
     * 通知通知栏
     */
    public void notifyChanged() {
        notificationManager.notify(id, getNotification());
    }

    /**
     * 取消通知栏
     */
    public void cancel() {
        notificationManager.cancel(id);
    }

}
