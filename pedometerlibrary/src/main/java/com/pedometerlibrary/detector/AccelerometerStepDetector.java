package com.pedometerlibrary.detector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.pedometerlibrary.common.PedometerParam;
import com.pedometerlibrary.util.DateUtil;
import com.pedometerlibrary.util.LogUtil;

import java.util.Date;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/12/17 23:03
 * <p>
 * AccelerometerStepDetector
 */

public class AccelerometerStepDetector implements SensorEventListener {
    private static final String TAG = AccelerometerStepDetector.class.getSimpleName();
    /**
     * 波峰波谷差值
     */
    private static final int VALUE_NUM = 4;
    /**
     * 动态阈值需要动态的数据，这个值用于这些动态数据的阈值
     */
    private static final float INITIAL_VALUE = 1.3f;
    /**
     * 存放计算阈值的波峰波谷差值
     */
    private static final float[] TEMP_VALUE = new float[VALUE_NUM];
    /**
     * 存放三轴数据
     */
    private static final float[] ORI_VALUES = new float[3];
    /**
     * 临时阀值索引
     */
    private int tempIndex = 0;
    /**
     * 是否上升的标志位
     */
    private boolean isDirectionUp = false;
    /**
     * 持续上升次数
     */
    private int continueUpCount = 0;
    /**
     * 上一点的持续上升的次数，为了记录波峰的上升次数
     */
    private int continueUpFormerCount = 0;
    /**
     * 上一点的状态，上升还是下降
     */
    private boolean lastStatus = false;
    /**
     * 波峰值
     */
    private float peakOfWave = 0;
    /**
     * 波谷值
     */
    private float valleyOfWave = 0;
    /**
     * 此次波峰的时间
     */
    private long timeOfThisPeak = 0;
    /**
     * 上次波峰的时间
     */
    private long timeOfLastPeak = 0;
    /**
     * 当前的时间
     */
    private long timeOfNow = 0;
    /**
     * 当前传感器的值
     */
    private float gravityNew = 0;
    /**
     * 上次传感器的值
     */
    private float gravityOld = 0;
    /**
     * 初始阈值
     */
    private float ThreadValue = 2.0f;

    /**
     * 波峰波谷时间差
     */
    private int timeInterval = 250;

    /**
     * 当前App步数
     */
    private int currentAppStep;

    /**
     * 最后一次传感器时间
     */
    private long lastSensorTime;

    /**
     * 临时步数
     */
    private int tempStep;

    /**
     * 最后峰值的时间
     */
    private long lastPeakTime;

    /**
     * 当前峰值时间
     */
    private long currentPeakTime;

    /**
     * Context
     */
    private Context context;

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

    public AccelerometerStepDetector(Context context) {
        this.context = context;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        init();
    }

    /**
     * 初始化参数
     */
    private void init() {
        currentAppStep = PedometerParam.getCurrentAppStep(context);
        lastSensorTime = PedometerParam.getLastSensorTime(context);

        if (isResetStep()) {
            currentAppStep = 0;
            lastSensorTime = DateUtil.getSystemTime();
            PedometerParam.setCurrentAppStep(context, currentAppStep);
            PedometerParam.setLastSensorTime(context, lastSensorTime);
            LogUtil.e(TAG, "执行重置步数");
        }
    }

    /**
     * 启动步测器（加速计传感器）
     */
    public void start() {
        Sensor accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerationSensor != null) {
            sensor = accelerationSensor;
            boolean isAvailable = sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_FASTEST);
            if (!isAvailable) {
                LogUtil.d(TAG, "Sensor.TYPE_ACCELEROMETER");
            }
            if (stepListener != null) {
                stepListener.onStep(currentAppStep);
            }
        } else {
            LogUtil.d(TAG, "The device does not support pedometer sensors");
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
     * 注入步数计算回调接口
     *
     * @param stepListener
     */
    public void setStepListener(StepListener stepListener) {
        this.stepListener = stepListener;
    }

    /**
     * 是否重置步数
     *
     * @return
     */
    private boolean isResetStep() {
        if (DateUtil.differentDays(lastSensorTime, DateUtil.getSystemTime()) > 0) {
            return true;
        }
        if (DateUtil.isMidnightTime(new Date())) {
            return true;
        }
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            for (int i = 0; i < 3; i++) {
                ORI_VALUES[i] = event.values[i];
            }
            gravityNew = (float) Math.sqrt(ORI_VALUES[0] * ORI_VALUES[0] +
                    ORI_VALUES[1] * ORI_VALUES[1] + ORI_VALUES[2] * ORI_VALUES[2]);
            detectorNewStep(gravityNew);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        LogUtil.v(TAG, "Sensor accuracy:" + accuracy);
    }

    /**
     * 检测步子，并开始记步
     * 1.传入sersor中的数据
     * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
     * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
     */
    private void detectorNewStep(float values) {
        if (gravityOld == 0) {
            gravityOld = values;
        } else {
            if (detectorPeak(values, gravityOld)) {
                timeOfLastPeak = timeOfThisPeak;
                timeOfNow = System.currentTimeMillis();
                if (timeOfNow - timeOfLastPeak >= timeInterval &&
                        (peakOfWave - valleyOfWave >= ThreadValue)) {
                    timeOfThisPeak = timeOfNow;
                    detectorValidStep();
                }
                if (timeOfNow - timeOfLastPeak >= timeInterval &&
                        (peakOfWave - valleyOfWave >= INITIAL_VALUE)) {
                    timeOfThisPeak = timeOfNow;
                    ThreadValue = peakValleyThread(peakOfWave - valleyOfWave);
                }
            }
        }
        gravityOld = values;
    }

    /**
     * 有效运动处理
     * 1.连续记录9才开始记步
     * 2.连续记录9步用户停住超过3秒，则前面的记录失效，下次从头开始
     * 3.连续记录了9步用户还在运动，之前的数据才有效
     */
    private void detectorValidStep() {
        lastPeakTime = currentPeakTime;
        currentPeakTime = System.currentTimeMillis();
        if (currentPeakTime - lastPeakTime < 3000L) {
            if (tempStep < 9) {
                tempStep++;
                if (isResetStep()) {
                    currentAppStep = tempStep;
                    lastSensorTime = DateUtil.getSystemTime();
                }
                PedometerParam.setCurrentAppStep(context, currentAppStep);
                PedometerParam.setLastSensorTime(context, lastSensorTime);
            } else if (tempStep == 9) {
                tempStep++;
                currentAppStep += tempStep;
                if (isResetStep()) {
                    currentAppStep = tempStep;
                    lastSensorTime = DateUtil.getSystemTime();
                }
                PedometerParam.setCurrentAppStep(context, currentAppStep);
                PedometerParam.setLastSensorTime(context, lastSensorTime);
                if (stepListener != null) {
                    stepListener.onStep(currentAppStep);
                }
            } else {
                currentAppStep++;
                if (isResetStep()) {
                    currentAppStep = 0;
                    lastSensorTime = DateUtil.getSystemTime();
                }
                PedometerParam.setCurrentAppStep(context, currentAppStep);
                PedometerParam.setLastSensorTime(context, DateUtil.getSystemTime());
                if (stepListener != null) {
                    stepListener.onStep(currentAppStep);
                }
            }
        } else {
            tempStep = 1;
        }
    }

    /**
     * 检测波峰
     * 以下四个条件判断为波峰：
     * 1.目前点为下降的趋势：isDirectionUp为false
     * 2.之前的点为上升的趋势：lastStatus为true
     * 3.到波峰为止，持续上升大于等于2次
     * 4.波峰值大于20
     * <p>
     * 记录波谷值
     * 1.观察波形图，可以发现在出现步子的地方，波谷的下一个就是波峰，有比较明显的特征以及差值
     * 2.所以要记录每次的波谷值，为了和下次的波峰做对比
     */
    private boolean detectorPeak(float newValue, float oldValue) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            isDirectionUp = true;
            continueUpCount++;
        } else {
            continueUpFormerCount = continueUpCount;
            continueUpCount = 0;
            isDirectionUp = false;
        }

        if (!isDirectionUp && lastStatus && (continueUpFormerCount >= 2 || oldValue >= 20)) {
            peakOfWave = oldValue;
            return true;
        } else if (!lastStatus && isDirectionUp) {
            valleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    /**
     * 阈值的计算
     * 1.通过波峰波谷的差值计算阈值
     * 2.记录4个值，存入tempValue[]数组中
     * 3.在将数组传入函数averageValue中计算阈值
     */
    private float peakValleyThread(float value) {
        float tempThread = ThreadValue;
        if (tempIndex < VALUE_NUM) {
            TEMP_VALUE[tempIndex] = value;
            tempIndex++;
        } else {
            tempThread = averageValue(TEMP_VALUE, VALUE_NUM);
            for (int i = 1; i < VALUE_NUM; i++) {
                TEMP_VALUE[i - 1] = TEMP_VALUE[i];
            }
            TEMP_VALUE[VALUE_NUM - 1] = value;
        }
        return tempThread;

    }

    /**
     * 梯度化阈值
     * 1.计算数组的均值
     * 2.通过均值将阈值梯度化在一个范围里
     */
    private float averageValue(float value[], int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / VALUE_NUM;
        if (ave >= 8)
            ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 2.0;
        else {
            ave = (float) 1.3;
        }
        return ave;
    }

    /**
     * 步测器（加速计传感器）回调接口
     */
    public interface StepListener {

        void onStep(int step);

        void onNotSupported();
    }
}
