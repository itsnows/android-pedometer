package com.pedometerlibrary.widget;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/20 15:15
 * <p>
 * EmptyPedometerNotification
 */
public class EmptyPedometerNotification extends PedometerNotification {
    private Context context;
    private int id;

    public static EmptyPedometerNotification with(Context context, int id) {
        return new EmptyPedometerNotification(context, id);
    }

    private EmptyPedometerNotification(Context context, int id) {
        this.context = context;
        this.id = id;
    }

    @Override
    public NotificationCompat.Builder getBuilder() {
        return null;
    }

    @Override
    public void notifyChanged() {

    }

    @Override
    public void cancel() {

    }
}
