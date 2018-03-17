package com.pedometerlibrary.widget;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/16 9:45
 * <p>
 * PedometerNotification
 */
public abstract class PedometerNotification {

    /**
     * 获取通知栏构建者
     *
     * @return NotificationCompat.Builder
     */
    public abstract NotificationCompat.Builder getBuilder();

    /**
     * 获取通知栏
     *
     * @return Notification
     */
    public Notification getNotification() {
        return getBuilder().build();
    }

    /**
     * 通知通知栏
     */
    public abstract void notifyChanged();

    /**
     * 取消通知栏
     */
    public abstract void cancel();

}
