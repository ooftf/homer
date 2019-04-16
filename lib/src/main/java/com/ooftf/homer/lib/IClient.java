package com.ooftf.homer.lib;

import android.net.Uri;

import com.ooftf.homer.lib.aidl.IpcRequestBody;
import com.ooftf.homer.lib.aidl.IpcResponseBody;

import io.reactivex.Single;

public interface IClient {
    Single<IpcResponseBody> call(final Uri uri, final IpcRequestBody requestBody);
}
