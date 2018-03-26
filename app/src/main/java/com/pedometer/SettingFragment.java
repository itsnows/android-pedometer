package com.pedometer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pedometerlibrary.common.PedometerOptions;
import com.pedometerlibrary.common.PedometerManager;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/21 17:00
 * <p>
 * StatusFragment
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rlNotificationSwitchParent;
    private SwitchCompat scNotificationSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        rlNotificationSwitchParent = view.findViewById(R.id.rl_fragment_setting_notification_switch_parent);
        rlNotificationSwitchParent.setOnClickListener(this);
        scNotificationSwitch = view.findViewById(R.id.sc_fragment_setting_notification_switch);
        if (PedometerManager.getInstance().getNotify() == PedometerOptions.Notify.EMPTY) {
            scNotificationSwitch.setChecked(false);
        } else {
            scNotificationSwitch.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_setting_notification_switch_parent:
                if (scNotificationSwitch.isChecked()) {
                    PedometerManager.getInstance().setNotify(PedometerOptions.Notify.EMPTY);
                    scNotificationSwitch.setChecked(false);
                } else {
                    PedometerManager.getInstance().setNotify(PedometerOptions.Notify.SIMPLE);
                    scNotificationSwitch.setChecked(true);
                }
                break;
            default:
                break;
        }
    }
}
