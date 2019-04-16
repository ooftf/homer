package com.ooftf.homer.lib.main;

import com.ooftf.homer.lib.AbsIpcClient;
import com.ooftf.homer.lib.annotation.IpcUriPath;

@IpcUriPath(value = "main.service")
public class IpcMainClient extends AbsIpcClient {
    @Override
    protected Class getServiceClass() {
        return IpcMainService.class;
    }
}
