package com.ooftf.homer.lib.main;

import com.ooftf.homer.lib.AbsIpcClient;
import com.ooftf.homer.lib.IpcConst;
import com.ooftf.homer.lib.annotation.IpcUriHost;

@IpcUriHost(value = IpcConst.MAIN_HOST)
public class IpcMainClient extends AbsIpcClient {
    @Override
    protected Class getServiceClass() {
        return IpcMainService.class;
    }
}
