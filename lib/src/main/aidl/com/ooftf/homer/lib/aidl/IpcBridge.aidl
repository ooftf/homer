// IpcBridge.aidl
package com.ooftf.homer.lib.aidl;

// Declare any non-default types here with import statements
import com.ooftf.homer.lib.aidl.IRemoteServiceCallback;
interface IpcBridge {
    void request(String methodName,String data,IRemoteServiceCallback callback);
}
