package com.pedometerlibrary;

import android.app.Application;
import android.util.Log;

import com.pedometerlibrary.common.PedometerManager;
import com.pedometerlibrary.common.PedometerOptions;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 22:37
 * <p>
 * Pedometer
 */

public class Pedometer {
    private static final String TAG = Pedometer.class.getSimpleName();

    private Pedometer() {
        throw new UnsupportedOperationException(
                "Pedometer cannot be instantiated");
    }

    /**
     * 初始化记步器
     *
     * @param application      Application
     * @param pedometerOptions 记步器选项
     */
    public static void initialize(Application application, PedometerOptions pedometerOptions) {
        if (!PedometerManager.isIsInitialized()) {
            PedometerManager manager = PedometerManager.getInstance();
            manager.initialize(application, pedometerOptions);
            Log.v(TAG, "Pedometer initialized");
        }
    }

    /**
     * 初始化自定义记步器
     *
     * @param application     Application
     * @param pedometerAction 记步器服务隐式意图
     *                        记步器服务必须继承com.pedometerlibrary.service.BasePedometerService
     */
    public static void initialize(Application application, String pedometerAction) {
        if (!PedometerManager.isIsInitialized()) {
            PedometerManager manager = PedometerManager.getInstance();
            manager.initialize(application, pedometerAction);
            Log.v(TAG, "Pedometer initialized");
        }
    }

}
