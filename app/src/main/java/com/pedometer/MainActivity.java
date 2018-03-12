package com.pedometer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pedometerlibrary.PedometerSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 计步器SDK初始化
        PedometerSDK.initialize(getApplication());
    }
}
