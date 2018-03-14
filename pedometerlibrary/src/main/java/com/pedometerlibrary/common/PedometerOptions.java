package com.pedometerlibrary.common;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/13 16:11
 * <p>
 * PedometerOptions
 */
public class PedometerOptions implements Serializable {

    /**
     * 计步器状态通知栏小图标
     */
    @DrawableRes
    private int samllIcon;

    /**
     * 计步器状态通知栏大图标
     */
    @DrawableRes
    private int largeIcon;

}
