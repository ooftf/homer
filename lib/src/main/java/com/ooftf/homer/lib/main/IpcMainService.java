package com.ooftf.homer.lib.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.ooftf.homer.lib.IpcPathManager;
import com.ooftf.homer.lib.RemoteCallback;
import com.ooftf.homer.lib.aidl.IRemoteServiceCallback;
import com.ooftf.homer.lib.aidl.IpcBridge;
import com.ooftf.homer.lib.aidl.IpcRequestBody;


public class IpcMainService extends Service {

    private final IpcBridge.Stub mBinder = new IpcBridge.Stub() {
        /**
         * 所有进程向主进程发出的请求在这里处理
         * @param requestBody
         * @param callback
         * @throws RemoteException
         */
        @Override
        public void request(IpcRequestBody requestBody, IRemoteServiceCallback callback) {
            try {
                IpcPathManager.handler(requestBody, new RemoteCallback(callback));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
