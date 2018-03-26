package com.pedometerlibrary.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/13 16:11
 * <p>
 * PedometerOptions
 */
public class PedometerOptions {

    /**
     * 记步器目标
     */
    private final int target;

    /**
     * 记步器状态通知栏小图标
     */
    private final int samllIcon;

    /**
     * 记步器状态通知栏大图标
     */
    private final int largeIcon;

    /**
     * 记步器通知栏
     */
    private final NotificationTheme notificationTheme;

    /**
     * 记步器通知栏意图
     */
    private final NotificationAction notificationAction;

    private PedometerOptions(Builder builder) {
        this.target = builder.target;
        this.samllIcon = builder.samllIcon;
        this.largeIcon = builder.largeIcon;
        this.notificationTheme = builder.notificationTheme;
        this.notificationAction = builder.notificationAction;
    }

    public enum NotificationTheme {
        EMPTY(),

        SIMPLE(),

        MINUTE()
    }

    public interface NotificationAction {
        void onAction(Context context, Bundle data);
    }

    public static class Builder {
        private int target;
        @DrawableRes
        private int samllIcon;
        @DrawableRes
        private int largeIcon;
        private NotificationTheme notificationTheme;
        private NotificationAction notificationAction;

        public Builder() {
        }

        public Builder setTarget(int target) {
            this.target = target;
            return this;
        }

        public Builder setSamllIcon(int samllIcon) {
            this.samllIcon = samllIcon;
            return this;
        }

        public Builder setLargeIcon(int largeIcon) {
            this.largeIcon = largeIcon;
            return this;
        }

        public Builder setNotificationTheme(NotificationTheme notificationTheme) {
            this.notificationTheme = notificationTheme;
            return this;
        }

        public Builder setNotificationAction(NotificationAction notificationAction) {
            this.notificationAction = notificationAction;
            return this;
        }

        public PedometerOptions build() {
            return new PedometerOptions(this);
        }
    }

}
