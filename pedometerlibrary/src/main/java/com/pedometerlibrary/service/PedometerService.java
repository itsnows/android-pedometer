package com.pedometerlibrary.service;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 15:54
 * <p>
 * PedometerService
 */
public class PedometerService extends BasePedometerService {


    @Override
    public void onStep() {

    }

    @Override
    public void onNotSupported() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
