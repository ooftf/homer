package com.ooftf.homer.sample;

import com.ooftf.homer.lib.AbsIpcClient;
import com.ooftf.homer.lib.annotation.IpcUriHost;

@IpcUriHost("other.IpcOtherClient.IpcOtherService")
public class IpcOtherClient extends AbsIpcClient {
    @Override
    protected Class getServiceClass() {
        return IpcOtherService.class;
    }
}
