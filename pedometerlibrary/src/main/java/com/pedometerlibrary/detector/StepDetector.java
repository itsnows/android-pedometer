package com.pedometerlibrary.detector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.Log;

import com.pedometerlibrary.common.PedometerParam;
import com.pedometerlibrary.util.DateUtil;

import java.util.Date;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 23:02
 * <p>
 * StepDetector
 */

public class StepDetector implements SensorEventListener {
    private static final String TAG = StepDetector.class.getSimpleName();
    private Context context;

    /**
     * 当前App步数
     */
    private int currentAppStep;

    /**
     * 最后一次记录传感器步数
     */
    private int lastSensorStep;

    /**
     * 最后一次步数偏移量
     */
    private int lastOffsetStep;

    /**
     * 最后一次传感器时间
     */
    private long lastSensorTime;

    /**
     * 系统启动时间
     */
    private long systemBootTime;

    /**
     * 系统重新启动状态
     */
    private boolean systemRebootStatus;

    /**
     * 是否重置记录
     */
    private boolean isReset;

    /**
     * 传感器管理
     */
    private SensorManager sensorManager;

    /**
     * 传感器
     */
    private Sensor sensor;

    /**
     * 步测器回调接口
     */
    private StepListener stepListener;

    public StepDetector(Context context) {
        this.context = context;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        init();
    }

    /**
     * 初始化参数
     */
    private void init() {
        lastSensorStep = PedometerParam.getLastSensorStep(context);
        lastSensorTime = PedometerParam.getLastSensorTime(context);
        lastOffsetStep = PedometerParam.getLastOffsetStep(context);
        currentAppStep = PedometerParam.getCurrentAppStep(context);
        systemBootTime = PedometerParam.getSystemBootTime(context);
        systemRebootStatus = PedometerParam.getSystemRebootStatus(context);

        // 系统是否重新启动
        // 1.开机或关机广播成立，则当前是否重启过，默认第一次启动为重启过
        // 2.开机或关机广播不成立，则当前系统运行时间小于最后一次记录的系统运行时间
        // 并且当前系统开机的时间是否等于最后一次记录的开机时间成立,则为重新启动过系统
        if (systemRebootStatus) {
            systemRebootStatus = true;
            Log.e(TAG, "初始化重启操作");
        }

        // 上一次传感器日期是否跨天
        Date lastDate = new Date(lastSensorTime);
        Date currentDate = new Date();
        if (!TextUtils.equals(DateUtil.dateToString(lastDate, "yyyy-MM-dd"), DateUtil.dateToString(currentDate, "yyyy-MM-dd"))) {
            isReset = true;

            lastSensorTime = DateUtil.getSystemTime();
            PedometerParam.setLastSensorTime(context, lastSensorTime);

            systemRebootStatus = false;
            PedometerParam.setSystemRebootStatus(context, systemRebootStatus);

            currentAppStep = 0;
            PedometerParam.setCurrentAppStep(context, currentAppStep);
            Log.e(TAG, "初始化跨天操作");
        }

        // 当前时间是否是零点
        if (DateUtil.isZeroTime(currentDate)) {
            isReset = true;

            systemRebootStatus = false;
            PedometerParam.setSystemRebootStatus(context, systemRebootStatus);

            currentAppStep = 0;
            PedometerParam.setCurrentAppStep(context, currentAppStep);
            Log.e(TAG, "初始化零点操作");
        }
    }

    /**
     * 启动步测器（API 步测器）
     */
    public void start() {
        // Sensor.TYPE_STEP_COUNTER 开机被激活后统计步数，重启手机后该数据清空 每次返回统计步数
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Sensor.TYPE_STEP_DETECTOR 临时步数，每次返回1.0
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if (countSensor != null) {
            Log.v(TAG, "Sensor.TYPE_STEP_COUNTER");
            sensor = countSensor;
            boolean isAvailable = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (!isAvailable) {
                Log.v(TAG, "Sensor.TYPE_STEP_COUNTER unavailable");
            }
        } else if (detectorSensor != null) {
            Log.v(TAG, "Sensor.TYPE_STEP_DETECTOR");
            sensor = detectorSensor;
            boolean isAvailable = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (!isAvailable) {
                Log.v(TAG, "Sensor.TYPE_STEP_DETECTOR unavailable");
            }
            if (stepListener != null) {
                stepListener.onStep(currentAppStep);
            }
        } else {
            Log.v(TAG, "The device does not support PedometerStep sensors");
            if (stepListener != null) {
                stepListener.onNotSupported();
            }
        }
    }

    /**
     * 停止步测器
     */
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    /**
     * 注入步测器回调接口
     *
     * @param stepListener
     */
    public void setStepDetector(StepListener stepListener) {
        this.stepListener = stepListener;
    }

    /**
     * 重置任务
     */
    public void reset(int tempStep) {
        currentAppStep = 0;
        PedometerParam.setCurrentAppStep(context, currentAppStep);

        lastOffsetStep = tempStep;
        PedometerParam.setLastOffsetStep(context, lastOffsetStep);

        systemBootTime = DateUtil.getSystemBootTime();
        PedometerParam.setSystemBootTime(context, systemBootTime);
        Log.e(TAG, "重置数据操作");
    }

    /**
     * 重启任务
     */
    public void reboot(int tempStep) {
        lastOffsetStep = tempStep - currentAppStep;
        PedometerParam.setLastOffsetStep(context, lastOffsetStep);

        systemRebootStatus = false;
        PedometerParam.setSystemRebootStatus(context, systemRebootStatus);

        systemBootTime = DateUtil.getSystemBootTime();
        PedometerParam.setSystemBootTime(context, systemBootTime);
        Log.e(TAG, "重启任务操作");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 当前传感器类型
        int sensorType = sensor.getType();
        if (sensorType == Sensor.TYPE_STEP_COUNTER) {
            int tempStep = (int) event.values[0];
            Log.d(TAG, "当前系统步数（TYPE_STEP_COUNTER）" + tempStep);
            if (isReset) {
                isReset = false;
                reset(tempStep);
            } else {
                if (systemRebootStatus) {
                    reboot(tempStep);
                }
            }

            if (!TextUtils.equals(DateUtil.dateToString(new Date(lastSensorTime), "yyyy-MM-dd"), DateUtil.dateToString(new Date(), "yyyy-MM-dd"))) {
                reset(tempStep);
            }

            if (DateUtil.isZeroTime(new Date())) {
                reset(tempStep);
            }

            int currentStep = tempStep - lastOffsetStep;

            if (currentStep < currentAppStep) {
                lastOffsetStep = tempStep - currentAppStep;
                PedometerParam.setLastOffsetStep(context, lastOffsetStep);
            } else {
                currentAppStep = currentStep;
            }

            lastSensorStep = tempStep;
            lastSensorTime = DateUtil.getSystemTime();

            if (currentAppStep < 0) {
                currentAppStep = 0;
                lastOffsetStep = currentAppStep;
                PedometerParam.setLastOffsetStep(context, lastOffsetStep);
            }
            PedometerParam.setCurrentAppStep(context, currentAppStep);
            PedometerParam.setLastSensorStep(context, lastSensorStep);
            PedometerParam.setLastSensorTime(context, lastSensorTime);
            if (stepListener != null) {
                stepListener.onStep(currentAppStep);
            }
        } else if (sensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                if (isReset) {
                    isReset = false;
                    lastSensorTime = DateUtil.getSystemTime();
                    currentAppStep = 0;
                }
                currentAppStep++;
                lastSensorTime = DateUtil.getSystemTime();
                PedometerParam.setCurrentAppStep(context, currentAppStep);
                PedometerParam.setLastSensorTime(context, lastSensorTime);
            }
            if (stepListener != null) {
                stepListener.onStep(currentAppStep);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v(TAG, "Sensor accuracy:" + accuracy);
    }

    /**
     * 步测器回调接口
     */
    public interface StepListener {

        void onStep(int step);

        void onNotSupported();
    }
}
