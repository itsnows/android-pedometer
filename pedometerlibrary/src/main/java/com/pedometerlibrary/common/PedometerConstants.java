package com.pedometerlibrary.common;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 22:37
 * <p>
 * PedometerConstants
 */

public class PedometerConstants {

    /**
     * 默认请求码
     */
    public static final int DEFAULT_REQUEST_CODE = 0x0000;

    /**
     * 连接记步服务请求码
     */
    public static final int REQUEST_CODE_CONNECT = 0x5230;

    /**
     * 断开连接记步服务请求码
     */
    public static final int REQUEST_CODE_DISCONNECT = 0x5231;

    /**
     * 记步状态空白通知栏（不显示通知栏）
     */
    public static final int PEDOMETER_NOTIFICATION_EMPTY = 0;

    /**
     * 记步状态简单通知栏
     */
    public static final int PEDOMETER_NOTIFICATION_SIMPLE = 1;

    /**
     * 记步状态全面通知栏
     */
    public static final int PEDOMETER_NOTIFICATION_MINUTE = 2;

}
