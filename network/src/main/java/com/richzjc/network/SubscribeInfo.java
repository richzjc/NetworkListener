package com.richzjc.network;

import java.util.List;

public interface SubscribeInfo {
    List<SubscribeMethod> availabeMethods();
    List<SubscribeMethod> loseMethods();
    List<SubscribeMethod> changeMethods();
}
