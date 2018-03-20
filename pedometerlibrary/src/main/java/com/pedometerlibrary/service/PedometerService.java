package com.pedometerlibrary.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.pedometerlibrary.R;
import com.pedometerlibrary.common.PedometerConstants;
import com.pedometerlibrary.common.PedometerParam;
import com.pedometerlibrary.receive.PedometerStatusActionReceiver;
import com.pedometerlibrary.widget.EmptyPedometerNotification;
import com.pedometerlibrary.widget.PedometerNotification;
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
    private static final String TAG = PedometerService.class.getSimpleName();
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
    /**
     * UI消息
     */
    public static final int MSG_UI = 0x1035;
    /**
     * 通知栏ID
     */
    private static final int NOTIFY_ID = 0x1002;
    /**
     * 记步通知栏
     */
    private PedometerNotification pedometerNotification;
    /**
     * 记步通知栏意图广播
     */
    private PedometerStatusActionReceiver statusActionReceiver;
    /**
     * 记步服务广播
     */
    private PedometerServiceReceiver serviceReceiver;
    /**
     * 记步服务端
     */
    private Messenger serverMessenger;
    /**
     * 记步服务端端消息处理
     */
    private ServeHanlder serveHanlder;
    /**
     * 记步客户端
     */
    private List<Messenger> clentMessengers;
    /**
     * 目標
     */
    private int target;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serverMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceReceiver != null) {
            serviceReceiver.unregisterReceiver(this);
        }
    }

    @Override
    public void onStep() {
        if (!serveHanlder.hasMessages(MSG_UI)) {
            serveHanlder.sendEmptyMessageDelayed(MSG_UI, 3000L);
        }
    }

    @Override
    public void onResumeStep() {
        serveHanlder.sendEmptyMessage(MSG_UI);
    }

    @Override
    public void onPauseStep() {

    }

    @Override
    public void onNotSupported() {
        pedometerNotification.cancel();
    }

    private void init() {
        if (serviceReceiver == null) {
            serviceReceiver = new PedometerServiceReceiver();
            serviceReceiver.registerReceiver(this);
        }

        if (serverMessenger == null) {
            serveHanlder = new ServeHanlder(this);
            serverMessenger = new Messenger(serveHanlder);
        }

        target = PedometerParam.getPedometerTarget(this);
        initNotify();
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        switch (PedometerParam.getPedometerNotification(this)) {
            case PedometerConstants.PEDOMETER_NOTIFICATION_EMPTY:
                pedometerNotification = EmptyPedometerNotification.with(this, NOTIFY_ID);
                break;
            case PedometerConstants.PEDOMETER_NOTIFICATION_SIMPLE:
                pedometerNotification = SimplePedometerNotification.with(this, NOTIFY_ID)
                        .setContentIntent((getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT)))
                        .setIcon(android.R.mipmap.sym_def_app_icon)
                        .setTitle(getString(R.string.notification_simple_pedometer_title, 0))
                        .setTitleColor(ContextCompat.getColor(this, R.color.notification_simple_pedometer_title_text_color))
                        .setTitleSize(getResources().getDimensionPixelOffset(R.dimen.notification_simple_pedometer_title_text_size))
                        .setDescription(getString(R.string.notification_simple_pedometer_description))
                        .setDescriptionColor(ContextCompat.getColor(this, R.color.notification_simple_pedometer_description_text_color))
                        .setDescriptionSize(getResources().getDimensionPixelOffset(R.dimen.notification_simple_pedometer_description_text_size))
                        .setMaxProgress(target)
                        .setCurrentProgress(0);
                break;
            case PedometerConstants.PEDOMETER_NOTIFICATION_MINUTE:

                break;
            default:
                break;
        }
    }

    /**
     * 发送消息到通知栏
     */
    private void sendMessageToNotify() {
        switch (PedometerParam.getPedometerNotification(this)) {
            case PedometerConstants.PEDOMETER_NOTIFICATION_EMPTY:
                break;
            case PedometerConstants.PEDOMETER_NOTIFICATION_SIMPLE:
                SimplePedometerNotification notification = (SimplePedometerNotification) this.pedometerNotification;
                int tempProgress = (int) ((float) getStep() / target * 100);
                int progress = (int) ((float) notification.getCurrentProgress() / notification.getMaxProgress() * 100);
                notification.setTitle(getString(R.string.notification_simple_pedometer_title, getStep()));
                if (notification.getCurrentProgress() == 0 || tempProgress > progress) {
                    notification.setCurrentProgress(getStep());
                }
                notification.notifyChanged();
                break;
            case PedometerConstants.PEDOMETER_NOTIFICATION_MINUTE:

                break;
            default:
                break;
        }
    }

    /**
     * 发送消息到客户端
     */
    private void sendMessageToClient() {
        if (clentMessengers == null) {
            return;
        }
        Message message = Message.obtain();
        message.what = MSG_SERVER;
        message.setData(new Bundle());
        try {
            Iterator<Messenger> iterator = clentMessengers.iterator();
            while (iterator.hasNext()) {
                Messenger messenger = iterator.next();
                if (messenger == null) {
                    iterator.remove();
                    break;
                }
                message.getData().putInt("step", getStep());
                messenger.send(message);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知意图
     */
    private PendingIntent getPendingIntent(int flag) {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, PedometerStatusActionReceiver.class), flag);
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
            final PedometerService service = weakReference.get();
            if (service == null) {
                return;
            }
            switch (msg.what) {
                case MSG_CLINT:
                    Messenger clintMessenger = msg.replyTo;
                    if (service.clentMessengers == null) {
                        service.clentMessengers = new ArrayList<>(0);
                    }
                    int code = msg.getData().getInt("code");
                    if (code == PedometerConstants.REQUEST_CODE_CONNECT) {
                        if (!service.clentMessengers.contains(clintMessenger)) {
                            service.clentMessengers.add(clintMessenger);
                            try {
                                Message message = Message.obtain();
                                message.what = MSG_SERVER;
                                message.setData(new Bundle());
                                message.getData().putInt("step", service.getStep());
                                clintMessenger.send(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (code == PedometerConstants.REQUEST_CODE_DISCONNECT) {
                        if (service.clentMessengers.contains(clintMessenger)) {
                            service.clentMessengers.remove(clintMessenger);
                        }
                    }
                case MSG_UI:
                    post(new Runnable() {
                        @Override
                        public void run() {
                            service.sendMessageToNotify();
                        }
                    });
                    post(new Runnable() {
                        @Override
                        public void run() {
                            service.sendMessageToClient();
                        }
                    });
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
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

        public void registerReceiver(Context context) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            intentFilter.addAction(Intent.ACTION_SHUTDOWN);
            intentFilter.addAction(ACTION_SYNC);
            intentFilter.addAction(ACTION_TARGET);
            context.registerReceiver(this, intentFilter);
        }

        public void unregisterReceiver(Context context) {
            context.unregisterReceiver(this);
        }

    }

}
