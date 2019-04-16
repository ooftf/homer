package com.ooftf.homer.lib;

import android.net.Uri;

import com.ooftf.homer.lib.aidl.IpcRequestBody;
import com.ooftf.homer.lib.aidl.IpcResponseBody;

import io.reactivex.Single;

public class IpcClient {
    public static Single<IpcResponseBody> request(String uri, IpcRequestBody body) {
        Uri parse = Uri.parse(uri);
        AbsIpcClient absIpcClient = IpcHostManager.getClientMap().get(parse.getHost());
        if (absIpcClient == null) {
            return Single.error(new IpcException("未找到 " + parse.getHost() + " 对应的AbsIpcClient", 502));
        }
        return absIpcClient.call(uri, body);
    }
}
