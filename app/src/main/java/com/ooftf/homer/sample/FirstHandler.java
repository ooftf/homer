package com.ooftf.homer.sample;

import android.os.RemoteException;
import android.support.annotation.Keep;

import com.ooftf.homer.lib.IpcCallback;
import com.ooftf.homer.lib.IpcHandler;
import com.ooftf.homer.lib.aidl.IpcRequestBody;
import com.ooftf.homer.lib.aidl.IpcResponseBody;
import com.ooftf.homer.lib.annotation.IpcUriPath;

@Keep
@IpcUriPath("/first")
public class FirstHandler implements IpcHandler {
    @Override
    public void handler(IpcRequestBody data, IpcCallback callback) throws RemoteException {
        callback.complete(new IpcResponseBody());
    }
}
