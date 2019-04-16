package com.ooftf.homer.lib;

import android.text.TextUtils;

import com.ooftf.homer.lib.annotation.IpcUriHost;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IpcHostManager {
    private static Map<String, AbsIpcClient> clientMap = new ConcurrentHashMap<>();

    public static void register(AbsIpcClient client) {
        IpcUriHost annotation = client.getClass().getAnnotation(IpcUriHost.class);
        if (annotation == null) {
            throw new RuntimeException(client.getClass().getName() + "is not add " + IpcUriHost.class.getName());
        }
        String name = annotation.value();
        if (TextUtils.isEmpty(name)) {
            throw new RuntimeException(client.getClass().getName() + "is not set value");
        }
        clientMap.put(name, client);
    }

    public static Map<String, AbsIpcClient> getClientMap() {
        return clientMap;
    }
}
