package com.ooftf.homer.lib;

import android.os.RemoteException;

import com.ooftf.homer.lib.aidl.IRemoteServiceCallback;

public class RemoteCallback implements IpcCallback {
    IRemoteServiceCallback remoteCallback;

    public RemoteCallback(IRemoteServiceCallback remoteCallback) {
        this.remoteCallback = remoteCallback;
    }

    public void complete(boolean success, String message) throws RemoteException {
        if (remoteCallback != null) {
            remoteCallback.complete(success, message);
        }
    }
}
