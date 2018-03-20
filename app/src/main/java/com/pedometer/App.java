package com.pedometer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pedometerlibrary.PedometerSDK;
import com.pedometerlibrary.common.PedometerOptions;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/20 17:27
 * <p>
 * App
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PedometerSDK.initialize(this,
                new PedometerOptions.Builder()
                        .setSamllIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(R.mipmap.ic_launcher)
                        .setNotificationTheme(PedometerOptions.NotificationTheme.SIMPLE)
                        .setNotificationAction(new PedometerOptions.NotificationAction() {
                            @Override
                            public void onAction(Context context, Bundle data) {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtras(data);
                                context.startActivity(intent);
                            }
                        })
                        .build());
    }


}
