package com.ooftf.homer.lib;

import android.os.RemoteException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class IpcRequestDispatcher {
    Map<String, IpcHandler> handlerMap = new ConcurrentHashMap<>();

    public IpcRequestDispatcher() {
        registerHandler();
    }

    abstract void registerHandler();

    void registerHandler(IpcHandler handler, String... methods) {
        for (String method : methods) {
            handlerMap.put(method, handler);
        }
    }

    public void handler(String name, String data, IpcCallback callback) throws RemoteException {
        IpcHandler ipcHandler = handlerMap.get(name);
        if (ipcHandler != null) {
            ipcHandler.handler(name, data, callback);
        } else {
            callback.complete(false, name + " in " + this.getClass().getSimpleName() + " Unregister");
        }
    }
}
