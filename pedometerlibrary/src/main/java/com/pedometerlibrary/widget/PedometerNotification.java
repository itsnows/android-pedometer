package com.pedometerlibrary.widget;

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
     * 通知通知栏
     */
    public abstract void notifyChanged();

    /**
     * 取消通知栏
     */
    public abstract void cancel();

}
