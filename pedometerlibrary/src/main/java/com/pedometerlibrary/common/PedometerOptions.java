package com.pedometerlibrary.common;

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
     * 记步器小图标
     */
    private final int samllIcon;

    /**
     * 记步器大图标
     */
    private final int largeIcon;

    /**
     * 记步器目标
     */
    private final int target;

    /**
     * 记步器通知栏
     */
    private final Notify notify;

    private PedometerOptions(Builder builder) {
        this.samllIcon = builder.samllIcon;
        this.largeIcon = builder.largeIcon;
        this.target = builder.target;
        this.notify = builder.notify;
    }

    public int getSamllIcon() {
        return samllIcon;
    }

    public int getLargeIcon() {
        return largeIcon;
    }

    public int getTarget() {
        return target;
    }

    public Notify getNotify() {
        return notify;
    }

    public enum Notify {
        EMPTY(),

        SIMPLE(),

        MINUTE()
    }

    public static class Builder {
        @DrawableRes
        private int samllIcon;
        @DrawableRes
        private int largeIcon;
        private Notify notify;
        private int target;

        public Builder() {
        }

        public Builder setSamllIcon(int samllIcon) {
            this.samllIcon = samllIcon;
            return this;
        }

        public Builder setLargeIcon(int largeIcon) {
            this.largeIcon = largeIcon;
            return this;
        }

        public Builder setTarget(int target) {
            this.target = target;
            return this;
        }

        public Builder setNotify(Notify notify) {
            this.notify = notify;
            return this;
        }

        public PedometerOptions build() {
            return new PedometerOptions(this);
        }
    }

}
