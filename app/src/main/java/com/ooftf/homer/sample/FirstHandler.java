package com.ooftf.homer.sample;

import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.Keep;

import com.ooftf.homer.lib.IpcCallback;
import com.ooftf.homer.lib.IpcClient;
import com.ooftf.homer.lib.IpcHandler;
import com.ooftf.homer.lib.aidl.IpcRequestBody;
import com.ooftf.homer.lib.aidl.IpcResponseBody;
import com.ooftf.homer.lib.annotation.IpcUriPath;

@Keep
@IpcUriPath("/FirstHandler")
public class FirstHandler implements IpcHandler {

    @Override
    public void handler(Uri uri, IpcRequestBody data, IpcCallback callback) throws RemoteException {
        String s = "ipc://main.service/FirstHandler?data=sss";
        IpcClient.request(s, null).subscribe();
        callback.complete(new IpcResponseBody());
    }

}
