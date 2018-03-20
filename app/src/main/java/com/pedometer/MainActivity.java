package com.pedometer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pedometerlibrary.PedometerSDK;
import com.pedometerlibrary.common.PedometerClient;
import com.pedometerlibrary.service.PedometerService;

public class MainActivity extends AppCompatActivity {
    private TextView tvStep;
    private PedometerClient pedometerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPedometer();
    }

    private void initView() {
        tvStep = findViewById(R.id.tv_activity_main_step);
    }

    /**
     * 计步器SDK初始化
     */
    private void initPedometer() {
        pedometerClient = PedometerClient.add(this, new PedometerService.CallBack() {
            @Override
            public void onStep(int step) {
                tvStep.setText(getString(R.string.activity_main_step, step));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pedometerClient != null && pedometerClient.isConnect()) {
            pedometerClient.remove();
        }
    }
}
