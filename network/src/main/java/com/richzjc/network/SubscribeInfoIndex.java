package com.richzjc.network;

import java.util.Map;

public interface SubscribeInfoIndex {
    Map<Class, SimpleSubscribeInfo> getSubscriberInfo();
}
