package com.ooftf.homer.lib;

import android.os.RemoteException;

public interface IpcHandler {
    void handler(String method, String data, IpcCallback callback) throws RemoteException;
}
