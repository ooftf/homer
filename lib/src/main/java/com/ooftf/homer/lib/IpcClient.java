package com.ooftf.homer.lib;

import android.net.Uri;

import com.ooftf.homer.lib.aidl.IpcRequestBody;
import com.ooftf.homer.lib.aidl.IpcResponseBody;

import java.lang.reflect.Type;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class IpcClient {
    public static Single<IpcResponseBody> request(IpcRequestBody body) {
        IClient client = IpcHostManager.getClientMap().get(body.getUri().getHost());
        if (!body.getUri().getScheme().equals(IpcConst.IPC_SCHEME)) {
            return Single.error(new IpcException("Uri scheme" + body.getUri().getScheme() + " is not " + IpcConst.IPC_SCHEME, 502));
        }
        if (client == null) {
            return Single.error(new IpcException("未找到 " + body.getUri().getHost() + " 对应的AbsIpcClient", 502));
        }
        return client.call(body);
    }


    /**
     * 请求到主进程，并JSON格式化返回结果
     *
     * @param body
     * @param type
     * @param <T>
     * @return
     */
    public <T> Single<T> request(final IpcRequestBody body, final Type type) {
        return request(body).map(new Function<IpcResponseBody, T>() {
            @Override
            public T apply(IpcResponseBody body) throws Exception {
                return Homer.getJsonParser().fromJson(body.getStringBody(), type);
            }
        });
    }

    /**
     * 请求到主进程，并JSON格式化返回结果
     *
     * @param body
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Single<T> request(final IpcRequestBody body, final Class<T> clazz) {
        return request(body).map(new Function<IpcResponseBody, T>() {
            @Override
            public T apply(IpcResponseBody s) throws Exception {
                return Homer.getJsonParser().fromJson(s.getStringBody(), clazz);
            }
        });
    }

    /**
     * 请求到主进程，并JSON格式化返回结果
     *
     * @param uri
     * @param type
     * @param <T>
     * @return
     */
    public <T> Single<T> request(final Uri uri, final Type type) {
        return request(uri).map(new Function<IpcResponseBody, T>() {
            @Override
            public T apply(IpcResponseBody body) throws Exception {
                return Homer.getJsonParser().fromJson(body.getStringBody(), type);
            }
        });
    }

    /**
     * 请求到主进程，并JSON格式化返回结果
     *
     * @param uri
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Single<T> request(final Uri uri, final Class<T> clazz) {
        return request(uri).map(new Function<IpcResponseBody, T>() {
            @Override
            public T apply(IpcResponseBody s) throws Exception {
                return Homer.getJsonParser().fromJson(s.getStringBody(), clazz);
            }
        });
    }

    public static Single<IpcResponseBody> request(Uri uri) {
        return request(new IpcRequestBody(uri));
    }

    public static Uri.Builder getBaseUri(String host) {
        return new Uri.Builder().scheme(IpcConst.IPC_SCHEME).authority(host);
    }
}
