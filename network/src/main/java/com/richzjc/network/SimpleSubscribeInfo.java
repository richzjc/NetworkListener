package com.richzjc.network;

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

    @Override
    public List<SubscribeMethod> availabeMethods() {
        return availableMethods;
    }

    @Override
    public List<SubscribeMethod> loseMethods() {
        return loseMethods;
    }

    @Override
    public List<SubscribeMethod> changeMethods() {
        return changeMethods;
    }
}
