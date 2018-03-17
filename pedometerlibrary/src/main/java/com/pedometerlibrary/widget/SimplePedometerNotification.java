package com.pedometerlibrary.widget;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.pedometerlibrary.R;
import com.pedometerlibrary.util.PackageManagerUtil;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/14 13:10
 * <p>
 * SimplePedometerNotification
 */
public class SimplePedometerNotification {
    private Context context;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder;
    private NotificationManager notifyManager;
    private CircleProgressDrawable drawable;
    private int id;

    public static SimplePedometerNotification with(Context context, int id) {
        return new SimplePedometerNotification(context, id);
    }

    private SimplePedometerNotification(Context context, int id) {
        this.context = context;
        this.id = id;
        init();
    }

    private void init() {
        notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_simple_pedometer);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(id), PackageManagerUtil.getAppName(context), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setShowBadge(true);
            channel.setSound(null, null);
            channel.setVibrationPattern(null);
            notifyManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, String.valueOf(id));
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        builder.setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(false)
                .setOngoing(true)
                .setDefaults(Notification.FLAG_AUTO_CANCEL)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setCustomBigContentView(remoteViews)
                .setCustomContentView(remoteViews);
        drawable = new CircleProgressDrawable(context.getResources());
    }

    public SimplePedometerNotification setContentIntent(PendingIntent intent) {
        builder.setContentIntent(intent);
        return this;
    }

    /**
     * 设置图标
     *
     * @param icon
     */
    public SimplePedometerNotification setIcon(@DrawableRes int icon) {
        remoteViews.setImageViewResource(R.id.iv_notification_simple_pedometer_icon, icon);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public SimplePedometerNotification setTitle(CharSequence title) {
        remoteViews.setTextViewText(R.id.tv_notification_simple_pedometer_title, title);
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public SimplePedometerNotification setTitleColor(@ColorInt int color) {
        remoteViews.setTextColor(R.id.tv_notification_simple_pedometer_title, color);
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param size
     */
    public SimplePedometerNotification setTitleSize(float size) {
        remoteViews.setTextViewTextSize(R.id.tv_notification_simple_pedometer_title, TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    /**
     * 设置描述
     *
     * @param description
     */
    public SimplePedometerNotification setDescription(CharSequence description) {
        remoteViews.setTextViewText(R.id.tv_notification_simple_pedometer_description, description);
        return this;
    }

    /**
     * 设置描述颜色
     *
     * @param color
     */
    public SimplePedometerNotification setDescriptionColor(@ColorInt int color) {
        remoteViews.setTextColor(R.id.tv_notification_simple_pedometer_description, color);
        return this;
    }

    /**
     * 设置描述颜色
     *
     * @param size
     */
    public SimplePedometerNotification setDescriptionSize(float size) {
        remoteViews.setTextViewTextSize(R.id.tv_notification_simple_pedometer_description, TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    /**
     * 设置进度
     */
    private void setProgress() {
        remoteViews.setImageViewBitmap(R.id.tv_notification_simple_pedometer_target_progress, drawable.getBitmap());
    }

    /**
     * 设置进度条宽度
     *
     * @param progressWidth
     */
    public SimplePedometerNotification setProgressWidth(float progressWidth) {
        drawable.setProgressWidth(progressWidth);
        setProgress();
        return this;
    }

    /**
     * 设置进度条半径
     *
     * @param progressRadius
     */
    public SimplePedometerNotification setProgressRadius(float progressRadius) {
        drawable.setProgressRadius(progressRadius);
        setProgress();
        return this;
    }

    /**
     * 设置进度条背景颜色
     *
     * @param progressBackgroundColor
     */
    public SimplePedometerNotification setProgressBackgroundColor(@ColorInt int progressBackgroundColor) {
        drawable.setProgressBackgroundColor(progressBackgroundColor);
        setProgress();
        return this;
    }

    /**
     * 设置进度条颜色
     *
     * @param progressColor
     */
    public SimplePedometerNotification setProgressColor(@ColorInt int progressColor) {
        drawable.setProgressColor(progressColor);
        setProgress();
        return this;
    }

    /**
     * 设置进度条数字大小
     *
     * @param progressNumberSize
     */
    public SimplePedometerNotification setProgressNumberSize(int progressNumberSize) {
        drawable.setProgressNumberSize(progressNumberSize);
        setProgress();
        return this;
    }

    /**
     * 设置进度条字体颜色
     *
     * @param progressNumberColor
     */
    public SimplePedometerNotification setProgressNumberColor(int progressNumberColor) {
        drawable.setProgressNumberColor(progressNumberColor);
        setProgress();
        return this;
    }

    /**
     * 设置进度条最大大进度
     *
     * @param maxProgress
     */
    public SimplePedometerNotification setMaxProgress(int maxProgress) {
        drawable.setMaxProgress(maxProgress);
        setProgress();
        return this;
    }

    /**
     * 设置进度当前进度
     *
     * @param currentProgress
     */
    public SimplePedometerNotification setCurrentProgress(int currentProgress) {
        drawable.setCurrentProgress(currentProgress);
        setProgress();
        return this;
    }

    /**
     * 获取进度条最大进度
     *
     * @return
     */
    public int getMaxProgress() {
        return drawable.getMaxProgress();
    }

    /**
     * 获取进度条当前进度
     *
     * @return
     */
    public int getCurrentProgress() {
        return drawable.getCurrentProgress();
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
     * 通知通知栏
     */
    public void notifyChanged() {
        notifyManager.notify(id, getNotification());
    }

    /**
     * 取消通知栏
     */
    public void cancel() {
        notifyManager.cancel(id);
    }


}
