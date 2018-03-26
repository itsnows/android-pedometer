package com.pedometer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pedometerlibrary.Pedometer;
import com.pedometerlibrary.common.PedometerOptions;
import com.pedometerlibrary.util.SystemUtil;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/20 17:27
 * <p>
 * App
 */
public class App extends Application {
    private static final String PROCESS_PEDOMETER = "com.pedometer";

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = SystemUtil.getProcessName(this);
        if (PROCESS_PEDOMETER.equals(processName)) {
            initDefault();
            // initCustom();
        }

        initDefault();
    }

    /**
     * 默认记步器
     */
    private void initDefault() {
        Pedometer.initialize(this,
                new PedometerOptions.Builder()
                        .setSamllIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(R.mipmap.ic_launcher)
                        .setNotificationTheme(PedometerOptions.NotificationTheme.SIMPLE)
                        .setNotificationAction(new PedometerOptions.NotificationAction() {
                            @Override
                            public void onAction(Context context, Bundle data) {
                                Intent intent = new Intent(context, StartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtras(data);
                                context.startActivity(intent);
                            }
                        })
                        .build());
    }

    /**
     * 自定义记步器
     */
    private void initCustom() {
        Pedometer.initialize(this, CustomPedometerService.ACTION);
    }

}
