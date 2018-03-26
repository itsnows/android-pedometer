package com.pedometerlibrary.widget;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.pedometerlibrary.R;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/14 13:10
 * <p>
 * SimplePedometerNotification
 */
public class SimplePedometerNotification extends PedometerNotification {
    private RemoteViews remoteViews;
    private CircleProgressDrawable drawable;

    public SimplePedometerNotification(Context context, int id) {
        super(context, id);
        init();
    }

    private void init() {
        remoteViews = new RemoteViews(getContext().getPackageName(), R.layout.notification_simple_pedometer);
        getBuilder().setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(false)
                .setOngoing(true)
                .setDefaults(Notification.FLAG_AUTO_CANCEL)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setCustomBigContentView(remoteViews)
                .setCustomContentView(remoteViews);
        drawable = new CircleProgressDrawable(getContext().getResources());
    }

    /**
     * 设置意图
     *
     * @param intent
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setContentIntent(PendingIntent intent) {
        getBuilder().setContentIntent(intent);
        return this;
    }

    /**
     * 设置图标
     *
     * @param icon
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setIcon(@DrawableRes int icon) {
        remoteViews.setImageViewResource(R.id.iv_notification_simple_pedometer_icon, icon);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title SimplePedometerNotification
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setTitle(CharSequence title) {
        remoteViews.setTextViewText(R.id.tv_notification_simple_pedometer_title, title);
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param color
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setTitleColor(@ColorInt int color) {
        remoteViews.setTextColor(R.id.tv_notification_simple_pedometer_title, color);
        return this;
    }

    /**
     * 设置标题颜色
     *
     * @param size
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setTitleSize(float size) {
        remoteViews.setTextViewTextSize(R.id.tv_notification_simple_pedometer_title, TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    /**
     * 设置描述
     *
     * @param description
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setDescription(CharSequence description) {
        remoteViews.setTextViewText(R.id.tv_notification_simple_pedometer_description, description);
        return this;
    }

    /**
     * 设置描述颜色
     *
     * @param color
     * @return SimplePedometerNotification
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
        remoteViews.setTextViewTextSize(R.id.tv_notification_simple_pedometer_description, TypedValue.COMPLEX_UNIT_PX, size);
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
     * @return SimplePedometerNotification
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
     * @return SimplePedometerNotification
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
     * @return SimplePedometerNotification
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
     * @return SimplePedometerNotification
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
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setProgressNumberColor(int progressNumberColor) {
        drawable.setProgressNumberColor(progressNumberColor);
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
     * 设置进度条最大大进度
     *
     * @param maxProgress
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setMaxProgress(int maxProgress) {
        drawable.setMaxProgress(maxProgress);
        setProgress();
        return this;
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
     * 设置进度当前进度
     *
     * @param currentProgress
     * @return SimplePedometerNotification
     */
    public SimplePedometerNotification setCurrentProgress(int currentProgress) {
        drawable.setCurrentProgress(currentProgress);
        setProgress();
        return this;
    }


}
