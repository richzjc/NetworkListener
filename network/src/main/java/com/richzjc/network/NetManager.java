package com.richzjc.network;

import android.content.Context;
import android.content.IntentFilter;

import com.richzjc.netannotation.nettype.NetType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.richzjc.network.ConstKt.ANDROID_NET_CHANGE_ACTION;

public class NetManager {

    private static HashMap<Object, SimpleSubscribeInfo> subscribeInfos = new HashMap<>();
    private static HashMap<Class, SimpleSubscribeInfo> callbackMethods = new HashMap<>();
    private static Context context;
    private static NetBroadCastReceiver netReceiver;

    public static void init(Context innerContext) {
        if (innerContext != null &&  netReceiver ==  null) {
            context = innerContext.getApplicationContext();
            netReceiver = new NetBroadCastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ANDROID_NET_CHANGE_ACTION);
            context.registerReceiver(netReceiver, filter);
        }
    }

    public static void addIndex(SubscribeInfoIndex index) {
        if (index != null)
            callbackMethods.putAll(index.getSubscriberInfo());
    }

    public static void bind(Object object) {
        if (context != null) {
            if (!subscribeInfos.containsKey(object.getClass()) && callbackMethods.containsKey(object.getClass())) {
                subscribeInfos.put(object, callbackMethods.get(object.getClass()));
            }
            SimpleSubscribeInfo info = subscribeInfos.get(object);
            notifySingle(object, info);
        }
    }

    public static void unBind(Object object) {
        subscribeInfos.remove(object);
    }

    public static void notifySingle(Object obj, SimpleSubscribeInfo info) {
        if (obj != null && info != null) {
            if (info.availabeMethods() != null && NetworkUtils.isNetworkAvailable(context)){
                for(SubscribeMethod subscribeMethod : info.availabeMethods()){
                    try {
                        Method method = obj.getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                        method.setAccessible(true);
                        method.invoke(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if(info.loseMethods() !=  null && NetworkUtils.getNetWorkType(context) == NetType.NONE){
                for(SubscribeMethod subscribeMethod : info.loseMethods()){
                    try {
                        Method method = obj.getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                        method.setAccessible(true);
                        method.invoke(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if(info.changeMethods() != null){
                NetType netType = NetworkUtils.getNetWorkType(context);
                for(SubscribeMethod subscribeMethod : info.changeMethods()){
                    if(subscribeMethod.getNetType() != NetType.AUTO && netType != subscribeMethod.getNetType())
                        continue;

                    if(subscribeMethod.getNetType() == NetType.AUTO && netType == NetType.NONE)
                        continue;

                    try {
                        Method method = obj.getClass().getDeclaredMethod(subscribeMethod.getMethodName());
                        method.setAccessible(true);
                        method.invoke(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void notifyAllChange(){
        if(context == null)
            return;
        for(Map.Entry<Object, SimpleSubscribeInfo> entry : subscribeInfos.entrySet()){
            notifySingle(entry.getKey(), entry.getValue());
        }
    }
}
