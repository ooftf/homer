// IpcBridge.aidl
package com.ooftf.homer.lib.aidl;

// Declare any non-default types here with import statements
import com.ooftf.homer.lib.aidl.IRemoteServiceCallback;
import com.ooftf.homer.lib.aidl.IpcRequestBody;
interface IpcBridge {
    void request(in IpcRequestBody data,IRemoteServiceCallback callback);
}
