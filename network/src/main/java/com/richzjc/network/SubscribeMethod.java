package com.richzjc.network;

import com.richzjc.netannotation.nettype.NetType;

public class SubscribeMethod {
    public String getMethodName() {
        return methodName;
    }

    public NetType getNetType() {
        return netType;
    }

    private String methodName;
    private NetType netType;

    public SubscribeMethod(String methodName, NetType type){
        this.methodName = methodName;
        this.netType = type;
    }
}
