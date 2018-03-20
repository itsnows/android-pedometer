package com.pedometerlibrary.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.pedometerlibrary.R;
import com.pedometerlibrary.common.PedometerParam;
import com.pedometerlibrary.receive.PedometerStatusActionReceiver;
import com.pedometerlibrary.util.IntentUtil;
import com.pedometerlibrary.widget.NotifyThme;
import com.pedometerlibrary.widget.SimplePedometerNotification;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 15:54
 * <p>
 * PedometerService
 */
public class PedometerService extends BasePedometerService {
    /**
     * 记步服务隐式意图
     */
    public static final String ACTION = "com.pedometerlibrary.service.PedometerService";
    /**
     * 记步服务同步数据隐式意图
     */
    public static final String ACTION_SYNC = "com.pedometerlibrary.service.PedometerService.ACTION_SYNC";
    /**
     * 记步服务目标设置隐式意图
     */
    public static final String ACTION_TARGET = "com.pedometerlibrary.service.PedometerService.ACTION_TARGET";
    /**
     * 服务端消息
     */
    public static final int MSG_SERVER = 0x1033;
    /**
     * 客服端消息
     */
    public static final int MSG_CLINT = 0x1034;
    private static final String TAG = PedometerService.class.getSimpleName();
    /**
     * 通知栏ID
     */
    private static final int NOTIFY_ID = 0x1002;

    /**
     * 记步通知栏
     */
    private SimplePedometerNotification spNotification;

    /**
     * 记步通知栏意图广播
     */
    private PedometerStatusActionReceiver pedometerStatusActionReceiver;

    /**
     * 记步服务端
     */
    private Messenger serverMessenger;

    /**
     * 客户端
     */
    private List<Messenger> clentMessengers;

    /**
     * 主题
     */
    private NotifyThme theme;

    /**
     * 目標
     */
    private int target;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        serverMessenger = new Messenger(new ServeHanlder(this));
        theme = NotifyThme.matched(PedometerParam.getPedometerNotifyTheme(this));
        target = PedometerParam.getPedometerNotifyTarget(this);
        initNotify();
    }

    /**
     * 初始化通知栏
     */
    @SuppressWarnings("deprecation")
    private void initNotify() {
        switch (theme) {
            case SIMPLE:
                spNotification = SimplePedometerNotification.with(this, NOTIFY_ID)
                        .setContentIntent((getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT)))
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setTitle(getString(R.string.notification_simple_pedometer_title, 0))
                        .setTitleColor(ContextCompat.getColor(this, R.color.notification_simple_pedometer_title_text_color))
                        .setTitleSize(getResources().getDimensionPixelOffset(R.dimen.notification_simple_pedometer_title_text_size))
                        .setDescription(getString(R.string.notification_simple_pedometer_description))
                        .setDescriptionColor(ContextCompat.getColor(this, R.color.notification_simple_pedometer_description_text_color))
                        .setDescriptionSize(getResources().getDimensionPixelOffset(R.dimen.notification_simple_pedometer_description_text_size))
                        .setMaxProgress(target)
                        .setCurrentProgress(0);
                spNotification.notifyChanged();
                break;
            case MINUTE:
                break;
            default:
                break;
        }
    }

    /**
     * 设置通知栏
     */
    public void setNotify() {
        switch (theme) {
            case SIMPLE:
                int tempProgress = (int) ((float) getStep() / target * 100);
                int progress = (int) ((float) spNotification.getCurrentProgress() / spNotification.getMaxProgress() * 100);
                if (tempProgress > progress) {
                    spNotification.setCurrentProgress(getStep());
                    spNotification.notifyChanged();
                }
                break;
            case MINUTE:
                break;
            default:
                break;
        }
    }

    /**
     * 通知意图
     */
    private PendingIntent getPendingIntent(int flag) {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, PedometerStatusActionReceiver.class), flag);
    }

    /**
     * 发送消息
     */
    private void sendMsgToClient(String tag) {
        if (clentMessengers == null) {
            return;
        }
        Message message = Message.obtain();
        message.what = MSG_SERVER;
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        try {
            Iterator<Messenger> iterator = clentMessengers.iterator();
            while (iterator.hasNext()) {
                Messenger messenger = iterator.next();
                if (messenger == null) {
                    iterator.remove();
                    break;
                }

                if ("step".equals(tag)) {
                    bundle.putInt("step", getStep());
                }
                message.setData(bundle);
                messenger.send(message);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStep() {


    }

    @Override
    public void onNotSupported() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serverMessenger.getBinder();
    }

    /**
     * 服务端消息处理
     */
    private static class ServeHanlder extends Handler {
        private WeakReference<PedometerService> weakReference;

        private ServeHanlder(PedometerService pedometerService) {
            this.weakReference = new WeakReference<>(pedometerService);
        }

        @Override
        public void handleMessage(Message msg) {
            PedometerService service = weakReference.get();
            if (service == null) {
                return;
            }
            switch (msg.what) {
                case MSG_CLINT:
                    Messenger clintMessenger = msg.replyTo;
                    if (service.clentMessengers == null) {
                        service.clentMessengers = new ArrayList<>(0);
                    }
                    String tag = msg.getData().getString("tag");
                    if ("connect".equals(tag)) {
                        if (!service.clentMessengers.contains(clintMessenger)) {
                            service.clentMessengers.add(clintMessenger);
                            try {
                                Message message = Message.obtain();
                                message.what = MSG_SERVER;
                                Bundle bundle = new Bundle();
                                bundle.putInt("step", service.getStep());
                                message.setData(bundle);
                                clintMessenger.send(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        return;
                    }
                    if ("disconnect".equals(tag)) {

                        return;
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 计步客服端
     */
    public static class Client {
        private Activity activity;
        private CallBack callBack;

        /**
         * 远程服务端
         */
        private Messenger serverMessenger;

        /**
         * 记步客服端
         */
        private Messenger clientMessenger;

        /**
         * 连接状态
         */
        private boolean isConnect;

        /**
         * 服务连接
         */
        private ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serverMessenger = new Messenger(service);
                connectServer();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serverMessenger = null;
            }
        };

        private Client(Activity activity, CallBack callBack) {
            this.activity = activity;
            this.callBack = callBack;
            Intent intent = IntentUtil.createExplicitFromImplicitIntent(activity, new Intent(PedometerService.ACTION));
            activity.bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT);

            ClientHanlder clientHanlder = new ClientHanlder(this);
            clientMessenger = new Messenger(clientHanlder);
        }

        public static Client add(Activity activity, CallBack callBack) {
            if (activity == null) {
                throw new IllegalArgumentException("Context can not be null");
            }
            return new Client(activity, callBack);
        }

        public void sync() {


        }

        public void target(int target) {
            if (activity == null) {
                if (callBack != null) {
                    callBack.onTarget(target);
                }
                return;
            }
            activity.sendBroadcast(new Intent(ACTION_TARGET));
        }

        public void remove() {
            if (isConnect && serverMessenger != null) {
                disconnectServer();
            }

            if (activity != null && serviceConnection != null) {
                activity.unbindService(serviceConnection);
            }
        }

        /**
         * 连接记步服务
         */
        private void connectServer() {
            try {
                Message message = Message.obtain();
                message.replyTo = clientMessenger;
                message.what = MSG_CLINT;
                Bundle bundle = new Bundle();
                bundle.putString("tag", "connect");
                message.setData(bundle);
                serverMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * 断开记步服务
         */
        private void disconnectServer() {
            try {
                Message message = Message.obtain();
                message.what = MSG_CLINT;
                Bundle bundle = new Bundle();
                bundle.putString("tag", "disconnect");
                message.setData(bundle);
                serverMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            isConnect = false;
        }

        /**
         * 客服端消息处理
         */
        public static class ClientHanlder extends Handler {
            private WeakReference<Client> weakReference;

            private ClientHanlder(Client client) {
                weakReference = new WeakReference<>(client);
            }

            @Override
            public void handleMessage(Message msg) {
                Client client = weakReference.get();
                if (client == null) {
                    return;
                }
                switch (msg.what) {
                    case PedometerService.MSG_SERVER:
                        if (!client.isConnect) {
                            client.isConnect = true;
                        }
                        if (client.callBack != null) {
                            Bundle bundle = msg.getData();
                            String tag = bundle.getString("tag");
                            if ("step".equals(tag)) {
                                int step = bundle.getInt("step", 0);
                                client.callBack.onStep(step);
                            }


                        }
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        }
    }

    /**
     * 计步服务回调接口
     */
    public static abstract class CallBack {

        // 计步探测器
        public abstract void onStep(int step);

        // 同步数据
        public void onSync(int isSucceed) {

        }

        // 同步目标
        public void onTarget(int target) {

        }

    }

    /**
     * 计步器控制广播
     */
    private class PedometerServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.d(TAG, "SCREEN ON");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.d(TAG, "SCREEN OFF");
            } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {
                Log.d(TAG, "ACTION_DATE_CHANGED");
            } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                Log.d(TAG, "ACTION_TIME_CHANGED");
            } else if (Intent.ACTION_TIME_TICK.equals(action)) {
                Log.d(TAG, "ACTION_TIME_TICK");
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                Log.d(TAG, "ACTION_CLOSE_SYSTEM_DIALOGS");
            } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
                Log.d(TAG, "ACTION_SHUTDOWN");
            } else if (ACTION_SYNC.equals(action)) {
                Log.d(TAG, "ACTION_SYNC");
            } else if (ACTION_TARGET.equals(action)) {
                Log.d(TAG, "ACTION_TARGET");
            }
        }
    }

}
