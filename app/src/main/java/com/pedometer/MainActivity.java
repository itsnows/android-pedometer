package com.pedometer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pedometerlibrary.PedometerSDK;
import com.pedometerlibrary.service.PedometerService;

public class MainActivity extends AppCompatActivity {

    private PedometerService.Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 计步器SDK初始化
        PedometerSDK.initialize(getApplication());
        client = PedometerService.Client.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.remove();
        }
    }
}
