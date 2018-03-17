package com.pedometer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pedometerlibrary.PedometerSDK;
import com.pedometerlibrary.service.PedometerService;

public class MainActivity extends AppCompatActivity {

    private PedometerService.Client client;
    private TextView tvStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 计步器SDK初始化
        PedometerSDK.initialize(getApplication());
        client = PedometerService.Client.add(this, new PedometerService.CallBack() {
            @Override
            public void onStep(int step) {
                tvStep.setText(getString(R.string.activity_main_step, step));
            }

            @Override
            public void onSync(boolean isSucceed) {
                super.onSync(isSucceed);
            }

            @Override
            public void onTarget(boolean isSucceed) {
                super.onTarget(isSucceed);
            }
        });

        tvStep = findViewById(R.id.tv_activity_main_step);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.remove();
        }
    }
}
