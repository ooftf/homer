package com.ooftf.homer.lib;

import android.os.RemoteException;

public interface IpcCallback {


     void complete(boolean success, String message) throws RemoteException;
}
