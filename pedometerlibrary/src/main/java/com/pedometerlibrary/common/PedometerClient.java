package com.pedometerlibrary.common;

import android.app.Activity;
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

import com.pedometerlibrary.service.PedometerService;
import com.pedometerlibrary.util.IntentUtil;

import java.lang.ref.WeakReference;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/20 14:11
 * <p>
 * 记步客户端
 */
public class PedometerClient {
    private Activity activity;
    private PedometerService.CallBack callBack;
    /**
     * 记步服务连接
     */
    private PedometerServiceConnection connection;
    /**
     * 记步服务端
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

    private PedometerClient(Activity activity, PedometerService.CallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;

        if (connection == null) {
            connection = new PedometerServiceConnection();
        }

        if (clientMessenger == null) {
            ClientHanlder clientHanlder = new ClientHanlder(this);
            clientMessenger = new Messenger(clientHanlder);
        }
        bindServer();
    }

    /**
     * 添加客服端
     *
     * @param activity Activity
     * @param callBack PedometerService.CallBack
     * @return 记步客服端
     */
    public static PedometerClient add(Activity activity, PedometerService.CallBack callBack) {
        if (activity == null) {
            throw new IllegalArgumentException("Context can not be null");
        }
        return new PedometerClient(activity, callBack);
    }

    /**
     * 同步数据
     */
    public void sync() {

    }

    /**
     * 设置目标
     *
     * @param target
     */
    public void target(int target) {
        if (activity == null) {
            if (callBack != null) {
                callBack.onTarget(target);
            }
            return;
        }
        activity.sendBroadcast(new Intent(PedometerService.ACTION_TARGET));
    }

    /**
     * 移除记步客服端
     */
    public void remove() {
        if (isConnect && serverMessenger != null) {
            disconnectServer();
        }

        if (activity != null && connection != null) {
            unbindServer();
        }
    }

    /**
     * 是否连接记步服务
     *
     * @return
     */
    public boolean isConnect() {
        return isConnect;
    }

    /**
     * 绑定记步服务
     */
    private void bindServer() {
        Intent intent = IntentUtil.createExplicitFromImplicitIntent(activity, new Intent(PedometerService.ACTION));
        activity.bindService(intent, connection, Context.BIND_ABOVE_CLIENT);
    }

    /**
     * 连接记步服务
     */
    private void connectServer() {
        try {
            Message message = Message.obtain();
            message.replyTo = clientMessenger;
            message.what = PedometerService.MSG_CLINT;
            message.setData(new Bundle());
            message.getData().putInt("code", PedometerConstants.REQUEST_CODE_CONNECT);
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
            message.what = PedometerService.MSG_CLINT;
            message.setData(new Bundle());
            message.getData().putInt("code", PedometerConstants.REQUEST_CODE_DISCONNECT);
            serverMessenger.send(message);
            isConnect = false;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解绑记步服务
     */
    private void unbindServer() {
        activity.unbindService(connection);
    }

    /**
     * 记步器服务连接
     */
    private class PedometerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serverMessenger = new Messenger(service);
            connectServer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connection = null;
        }
    }

    /**
     * 客服端消息处理
     */
    public static class ClientHanlder extends Handler {
        private WeakReference<PedometerClient> weakReference;

        private ClientHanlder(PedometerClient client) {
            weakReference = new WeakReference<>(client);
        }

        @Override
        public void handleMessage(Message msg) {
            PedometerClient client = weakReference.get();
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
                        int step = bundle.getInt("step", 0);
                        if (client.callBack != null) {
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
