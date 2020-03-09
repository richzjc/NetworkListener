package com.richzjc.network;

import androidx.annotation.Nullable;


import java.util.List;

public class SimpleSubscribeInfo implements SubscribeInfo{

    private List<SubscribeMethod> availableMethods;
    private List<SubscribeMethod> loseMethods;
    private List<SubscribeMethod> changeMethods;

    public SimpleSubscribeInfo(List<SubscribeMethod> availableMethods, List<SubscribeMethod> loseMethods, List<SubscribeMethod> changeMethods){
        this.availableMethods = availableMethods;
        this.loseMethods = loseMethods;
        this.changeMethods = changeMethods;
    }

    @Nullable
    @Override
    public List<SubscribeMethod> availabeMethods() {
        return availableMethods;
    }

    @Nullable
    @Override
    public List<SubscribeMethod> loseMethods() {
        return loseMethods;
    }

    @Nullable
    @Override
    public List<SubscribeMethod> changeMethods() {
        return changeMethods;
    }
}
