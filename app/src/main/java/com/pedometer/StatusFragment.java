package com.pedometer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedometerlibrary.common.PedometerClient;
import com.pedometerlibrary.service.PedometerService;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/21 17:00
 * <p>
 * StatusFragment
 */
public class StatusFragment extends Fragment {
    private Context context;
    private TextView tvStep;
    private PedometerClient pedometerClient;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pedometerClient = PedometerClient.add((Activity) context, new PedometerService.CallBack() {
            @Override
            public void onStep(int step) {
                if (context != null) {
                    tvStep.setText(context.getString(R.string.activity_main_step, step));
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pedometerClient != null && pedometerClient.isConnect()) {
            pedometerClient.remove();
        }
    }

    private void initView(View view) {
        tvStep = view.findViewById(R.id.tv_fragment_status_step);
    }

}
