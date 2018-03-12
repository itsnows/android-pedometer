package com.pedometerlibrary.detector;

import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/18 0:12
 * <p>
 * StepDetectorManager
 */

public class StepDetectorManager {
    private static final String TAG = StepDetectorManager.class.getSimpleName();
    private Context context;

    /**
     * 加速度步测器
     */
    private AccelerometerStepDetector accelerometerStepDetector;

    /**
     * 步测器
     */
    private StepDetector stepDetector;

    /**
     * 步测器数据管理回调接口
     */
    private CallBack callBack;

    public StepDetectorManager(Context context) {
        this.context = context;
    }

    /**
     * 启动步测器
     */
    public void start() {
        // API版本大于或等于19 启用步测器，否则模拟步测器（加速计传感器）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startStepDetector();
        } else {
            startAccelerometerStepDetector();
        }
    }

    /**
     * 停止步测器
     */
    public void stop() {
        if (accelerometerStepDetector != null) {
            accelerometerStepDetector.stop();
        }

        if (stepDetector != null) {
            stepDetector.stop();
        }
    }

    /**
     * 启动模拟步测器
     */
    private void startAccelerometerStepDetector() {
        if (accelerometerStepDetector != null) {
            accelerometerStepDetector.stop();
            accelerometerStepDetector = null;
        }

        accelerometerStepDetector = new AccelerometerStepDetector(context);
        accelerometerStepDetector.setStepListener(new AccelerometerStepDetector.StepListener() {
            @Override
            public void onStep(int step) {
                if (callBack != null) {
                    callBack.onStep(step);
                }
                Log.v(TAG, String.valueOf(step));
            }

            @Override
            public void onNotSupported() {
                // 该设备不支持步测器
                if (callBack != null) {
                    callBack.onNotSupported();
                }
            }
        });
        accelerometerStepDetector.start();
    }

    /**
     * 启动步测器
     */
    private void startStepDetector() {
        if (stepDetector != null) {
            stepDetector.stop();
            stepDetector = null;
        }

        stepDetector = new StepDetector(context);
        stepDetector.setStepDetector(new StepDetector.StepListener() {
            @Override
            public void onStep(int step) {
                if (callBack != null) {
                    callBack.onStep(step);
                }
            }

            @Override
            public void onNotSupported() {
                // 设备不支持步测器，启动加速计传感器步测器
                startAccelerometerStepDetector();
            }
        });
        stepDetector.start();
    }

    /**
     * 注入步测器数据管理回调接口
     *
     * @param callBack
     */
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 步测器管理回调接口
     */
    public interface CallBack {

        void onStep(int step);

        void onNotSupported();
    }

}
