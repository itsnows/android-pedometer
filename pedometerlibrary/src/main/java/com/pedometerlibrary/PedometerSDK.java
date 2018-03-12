package com.pedometerlibrary;

import android.app.Application;
import android.util.Log;

import com.pedometerlibrary.common.PedometerManager;
import com.pedometerlibrary.service.PedometerService;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 22:37
 * <p>
 * PedometerSDK
 */

public class PedometerSDK {
    private static final String TAG = PedometerSDK.class.getSimpleName();

    private PedometerSDK() {
        throw new UnsupportedOperationException(
                "PedometerSDK cannot be instantiated");
    }

    /**
     * 初始化计步器
     *
     * @param application
     */
    public static void initialize(Application application) {
        initialize(application, PedometerService.ACTION);
    }

    /**
     * 初始化自定义计步器
     *
     * @param application
     * @param pedometerAction 记步器服务隐式意图
     *                        记步器服务必须继承com.pedometerlibrary.service.BasePedometerService
     */
    public static void initialize(Application application, String pedometerAction) {
        PedometerManager.setApplication(application);
        PedometerManager.setPedometerServiceAction(pedometerAction);
        PedometerManager.setZeroClockAlarm();
        PedometerManager.setJobScheduler();
        PedometerManager.startPedometerService();
        Log.v(TAG, "PedometerSDK initialized");
    }

}
