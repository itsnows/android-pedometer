package com.pedometerlibrary.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.pedometerlibrary.receive.PedometerNotifyActionReceiver;
import com.pedometerlibrary.util.IntentUtil;

import java.lang.ref.WeakReference;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 15:54
 * <p>
 * PedometerService
 */
public class PedometerService extends BasePedometerService {
    private static final String TAG = PedometerService.class.getSimpleName();
    /**
     * 记步服务隐式意图
     */
    public static final String ACTION = "com.pedometerlibrary.service.PedometerService";

    /**
     * 服务端消息
     */
    public static final int MSG_SERVER = 0x1033;

    /**
     * 客服端连接消息
     */
    public static final int MSG_CLINT_CONNECT = 0x1034;

    /**
     * 客服端断开连接消息
     */
    public static final int MSG_CLINT_DISCONNECT = 0x1035;

    /**
     * 通知栏ID
     */
    private static final int NOTIFY_ID = 0x1002;

    /**
     * 记步通知栏
     */
    private Notification notification;
    private NotificationManager notifyManager;

    /**
     * 记步通知栏意图广播
     */
    private PedometerNotifyActionReceiver pedometerNotifyActionReceiver;

    /**
     * 记步服务端
     */
    private Messenger serverMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        serverMessenger = new Messenger(new ServeHanlder(this));
    }

    /**
     * 初始化通知栏
     */
    @SuppressWarnings("deprecation")
    private void initNotify() {
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_runenpedometer);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(NOTIFY_ID), getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setShowBadge(true);
            channel.setSound(null, null);
            channel.setVibrationPattern(null);
            notifyManager.createNotificationChannel(channel);
            notification = new Notification.Builder(getApplicationContext(), String.valueOf(NOTIFY_ID))
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setDefaults(Notification.FLAG_AUTO_CANCEL)
                    .setSmallIcon(android.R.mipmap.icon)
                    .setCustomBigContentView(remoteViews)
                    .setCustomContentView(remoteViews)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(this)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setDefaults(Notification.FLAG_AUTO_CANCEL)
                    .setSmallIcon(R.mipmap.icon)
                    .setCustomBigContentView(remoteViews)
                    .setCustomContentView(remoteViews)
                    .build();
        }
    }

    /**
     * 通知意图
     */
    private PendingIntent getPendingIntent(int flag) {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, PedometerNotifyActionReceiver.class), flag);
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
                case MSG_CLINT_CONNECT:
                    break;
                case MSG_CLINT_DISCONNECT:
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

        private Client(Activity activity) {
            this.activity = activity;
            Intent intent = IntentUtil.createExplicitFromImplicitIntent(activity, new Intent(PedometerService.ACTION));
            activity.bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT);

            ClientHanlder clientHanlder = new ClientHanlder(this);
            clientMessenger = new Messenger(clientHanlder);
        }

        /**
         * 客服端消息处理
         */
        public static class ClientHanlder extends Handler {
            private WeakReference<Client> weakReference;

            public ClientHanlder(Client client) {
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
                        client.isConnect = true;
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        }

        public static Client add(Activity activity) {
            if (activity == null) {
                throw new IllegalArgumentException("Context can not be null");
            }
            return new Client(activity);
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
                message.what = MSG_CLINT_CONNECT;
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
                message.what = MSG_CLINT_DISCONNECT;
                serverMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            isConnect = false;
        }

    }

}
