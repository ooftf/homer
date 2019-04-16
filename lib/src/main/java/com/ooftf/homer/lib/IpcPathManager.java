package com.ooftf.homer.lib;

import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;

import com.ooftf.homer.lib.aidl.IpcRequestBody;
import com.ooftf.homer.lib.aidl.IpcResponseBody;
import com.ooftf.homer.lib.annotation.IpcUriPath;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IpcPathManager {
    static Map<String, IpcHandler> handlerMap = new ConcurrentHashMap<>();

    public static void register(IpcHandler ipcHandler) {
        IpcUriPath annotation = ipcHandler.getClass().getAnnotation(IpcUriPath.class);
        if (annotation == null) {
            throw new RuntimeException(ipcHandler.getClass().getName() + "is not add " + IpcUriPath.class.getName());
        }
        String name = annotation.value();
        if (TextUtils.isEmpty(name)) {
            throw new RuntimeException(ipcHandler.getClass().getName() + "is not set value");
        }
        handlerMap.put(name, ipcHandler);
    }

    public static void handler(String uri, IpcRequestBody data, IpcCallback callback) throws RemoteException {
        Uri parse = Uri.parse(uri);
        IpcHandler ipcHandler = handlerMap.get(parse.getPath());
        if (ipcHandler != null) {
            ipcHandler.handler(Uri.parse(uri), data, callback);
        } else {
            IpcResponseBody responseBody = new IpcResponseBody();
            responseBody.setCode(404);
            responseBody.setMessage(parse.getPath() + " in " + IpcPathManager.class.getSimpleName() + " Unregister");
            callback.complete(responseBody);
        }
    }
}