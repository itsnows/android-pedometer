package com.pedometerlibrary.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.pedometerlibrary.service.PedometerService;

import java.lang.ref.WeakReference;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/13 16:35
 * <p>
 * PedometerClient
 */
public class PedometerClient {
    private Activity activity;

    /**
     * 远程服务端
     */
    private Messenger serverMessenger;

    /**
     * 记步客服端
     */
    private Messenger clientMessenger;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serverMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serverMessenger = null;
        }
    };

    /**
     * 客服端消息处理
     */
    public static class ClientHanlder extends Handler {
        private WeakReference<Activity> weakReference;

        public ClientHanlder(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = weakReference.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case PedometerService.MSG_SERVER:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);

            super.handleMessage(msg);
        }
    }


    public void add(Activity activity) {


    }

    public void remove(Activity activity) {

    }

}
