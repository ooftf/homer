package com.ooftf.homer.lib;

import android.os.RemoteException;

import com.ooftf.homer.lib.aidl.IRemoteServiceCallback;
import com.ooftf.homer.lib.aidl.IpcResponseBody;

public class RemoteCallback implements IpcCallback {
    private IRemoteServiceCallback remoteCallback;

    public RemoteCallback(IRemoteServiceCallback remoteCallback) {
        this.remoteCallback = remoteCallback;
    }

    @Override
    public void complete(IpcResponseBody message) throws RemoteException {
        if (remoteCallback != null) {
            remoteCallback.complete(message);
        }
    }
}
