package com.richzjc.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(ConstKt.LOG_TAG, "异常了");
            return;
        }

        // 处理广播事件
        if (intent.getAction().equalsIgnoreCase(ConstKt.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(ConstKt.LOG_TAG, "网络发生改变");
            if (NetworkUtils.isNetworkAvailable(context)) {
                Log.e(ConstKt.LOG_TAG, "网络连接成功");
            } else {
                Log.e(ConstKt.LOG_TAG, "没有网络连接");
            }
            NetManager.notifyAllChange();
        }
    }
}
