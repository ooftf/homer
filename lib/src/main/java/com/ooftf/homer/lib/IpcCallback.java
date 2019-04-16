package com.ooftf.homer.lib;

import android.os.RemoteException;

import com.ooftf.homer.lib.aidl.IpcResponseBody;

public interface IpcCallback {


     void complete(IpcResponseBody message) throws RemoteException;
}
