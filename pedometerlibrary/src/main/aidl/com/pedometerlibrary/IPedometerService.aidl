// IPedometerService.aidl
package com.pedometerlibrary;

// Declare any non-default types here with import statements

interface IPedometerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    // 计步器步数（返回-1不支持计步器功能，否则返回计步器记录总步数）
    int onStep();

    // 设置目标
    void setTarget(int value);

}
