// IRemoteServiceCallback.aidl
package com.ooftf.homer.lib.aidl;
import com.ooftf.homer.lib.aidl.IpcResponseBody;
// Declare any non-default types here with import statements

interface IRemoteServiceCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void complete(in IpcResponseBody message);
}
