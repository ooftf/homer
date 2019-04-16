package com.ooftf.homer.lib;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ooftf.homer.lib.aidl.IRemoteServiceCallback;
import com.ooftf.homer.lib.aidl.IpcBridge;
import com.ooftf.homer.lib.aidl.IpcRequestBody;
import com.ooftf.homer.lib.aidl.IpcResponseBody;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * 跨进程通讯Client
 * <p>
 * ipc://进程名/方法名?key=value
 */
public abstract class AbsIpcClient implements IClient {
    /**
     * 用于延迟订阅
     */
    PublishSubject<Integer> lifeEvent = PublishSubject.create();
    ServiceConnection connection;
    IpcBridge mService;
    Boolean isSelfProcess;
    /**
     * 记录当前的状态
     */
    int state = State.UNCONNECTED;

    public AbsIpcClient() {
        String serviceProcessName = getServiceProcessName();
        if (serviceProcessName == null) {
            isSelfProcess = false;
        } else {
            isSelfProcess = getServiceProcessName().equals(ProcessUtils.getCurrentProcessName());
        }
    }

    /**
     * 处理绑定服务的请求
     */
    @SuppressLint("CheckResult")
    private void bindService() {
        Observable<String> bindObservable = Observable.just("");
        if (state == State.DISCONNECTING) {
            /**
             * 如果是正在断开连接状态，绑定事件等到解除绑定完成进行
             */
            bindObservable = bindObservable
                    .delay(new Function<String, ObservableSource<Integer>>() {
                        @Override
                        public ObservableSource<Integer> apply(String s) throws Exception {
                            return lifeEvent.filter(new Predicate<Integer>() {
                                @Override
                                public boolean test(Integer integer) throws Exception {
                                    return integer.equals(Event.ON_SERVICE_DISCONNECTED);
                                }
                            });
                        }
                    });
        }
        bindObservable
                // 如果多个绑定事件在等待队列，为了防止进行多次绑定，判断当前是否是UNCONNECTED状态
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return state == State.UNCONNECTED;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        realBindService();
                    }
                });
    }

    /**
     * 真正实现绑定的方法
     */
    private void realBindService() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e("AbsIpcClient", "onServiceConnected::" + name);
                mService = IpcBridge.Stub.asInterface(service);
                state = State.CONNECTED;
                lifeEvent.onNext(Event.ON_SERVICE_CONNECTED);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("AbsIpcClient", "onServiceDisconnected::" + name);
                state = State.UNCONNECTED;
                lifeEvent.onNext(Event.ON_SERVICE_DISCONNECTED);
            }
        };
        Intent intent = new Intent(Homer.getApplication(), getServiceClass());
        intent.setAction(getServiceClass().getName());
        Homer.getApplication().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        state = State.CONNECTING;
    }

    abstract protected Class getServiceClass();

    /**
     * 解除绑定
     */
    public void unBindService() {
        if (state == State.CONNECTED || state == State.CONNECTING) {
            Homer.getApplication().unbindService(connection);
            state = State.DISCONNECTING;
        }

    }

    /**
     * 请求到主进程，并JSON格式化返回结果
     *
     * @param uri
     * @param body
     * @param type
     * @param <T>
     * @return
     */
    public <T> Single<T> callToBean(final Uri uri, final IpcRequestBody body, final Type type) {
        return call(uri, body).map(new Function<IpcResponseBody, T>() {
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
     * @param body
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Single<T> callToBean(final Uri uri, final IpcRequestBody body, final Class<T> clazz) {
        return call(uri, body).map(new Function<IpcResponseBody, T>() {
            @Override
            public T apply(IpcResponseBody s) throws Exception {
                return Homer.getJsonParser().fromJson(s.getStringBody(), clazz);
            }
        });
    }

    @Nullable
    protected String getServiceProcessName() {
        try {
            ServiceInfo serviceInfo = Homer.getApplication().getPackageManager().getServiceInfo(new ComponentName(Homer.getApplication(), getServiceClass()), PackageManager.GET_META_DATA);
            return serviceInfo.processName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求到对应进程
     *
     * @param uri
     * @param requestBody
     * @return
     */
    public Single<IpcResponseBody> call(final Uri uri, final IpcRequestBody requestBody) {
        if (isSelfProcess) {
            /**
             * 处理请求
             */
            return localRequest(uri, requestBody);
        }
        /**
         * 如果当前是未绑定或者正在解绑状态，则开始绑定服务
         */
        if (state == State.UNCONNECTED || state == State.DISCONNECTING) {
            bindService();
        }
        /**
         * 处理请求
         */
        return remoteRequest(uri, requestBody);

    }

    /**
     * 跨进程请求
     *
     * @param uri
     * @param requestBody
     * @return
     */
    private Single<IpcResponseBody> remoteRequest(final Uri uri, final IpcRequestBody requestBody) {
        /**
         * 处理请求
         */
        Single<IpcResponseBody> observable = Single
                .create(new SingleOnSubscribe<IpcResponseBody>() {
                    @Override
                    public void subscribe(final SingleEmitter<IpcResponseBody> emitter) throws Exception {
                        HomerLog.log("ipc-remote-request", "value:" + uri + ",data:" + requestBody);
                        mService.request(uri, requestBody, new IRemoteServiceCallback.Stub() {
                            @Override
                            public void complete(IpcResponseBody message) throws RemoteException {
                                if (message.getCode() == 200) {
                                    HomerLog.log("ipc-remote-response-success", "value:" + uri + ",message:" + message);
                                    emitter.onSuccess(message);
                                } else {
                                    HomerLog.log("ipc-remote-response-fail", "value:" + uri + ",message:" + message);
                                    emitter.onError(new IpcException(message.getMessage(), message.getCode()));
                                }
                            }

                        });
                    }
                })
                .timeout(60, TimeUnit.SECONDS);
        if (state != State.CONNECTED) {
            //如果当前处于正在绑定状态，则等到绑定完成再执行请求
            return observable
                    .delaySubscription(lifeEvent.filter(new Predicate<Integer>() {
                        @Override
                        public boolean test(Integer integer) throws Exception {
                            return integer.equals(Event.ON_SERVICE_CONNECTED);
                        }
                    }));
        }
        return observable;
    }


    /**
     * 没有跨进程
     *
     * @param name
     * @param requestBody
     * @return
     */
    private Single<IpcResponseBody> localRequest(final Uri name, final IpcRequestBody requestBody) {
        return Single
                .create(new SingleOnSubscribe<IpcResponseBody>() {
                    @Override
                    public void subscribe(final SingleEmitter<IpcResponseBody> emitter) throws Exception {
                        HomerLog.log("ipc-local-request", "value:" + name + ",data:" + requestBody);
                        IpcPathManager.handler(name, requestBody, new IpcCallback() {
                            @Override
                            public void complete(IpcResponseBody message) throws RemoteException {
                                if (message.getCode() == 200) {
                                    HomerLog.log("ipc-local-response-success", "value:" + name + ",message:" + message);
                                    emitter.onSuccess(message);
                                } else {
                                    HomerLog.log("ipc-local-response-fail", "value:" + name + ",message:" + message);
                                    emitter.onError(new IpcException(message.getMessage(), message.getCode()));
                                }
                            }
                        });
                    }
                })
                .timeout(60, TimeUnit.SECONDS);
    }

    /**
     * 标识当前连接状态
     */
    interface State {
        int UNCONNECTED = 0;
        int CONNECTING = 1;
        int CONNECTED = 2;
        int DISCONNECTING = 3;
    }

    /**
     * 连接事件
     */
    interface Event {
        int ON_SERVICE_CONNECTED = 0;
        int ON_SERVICE_DISCONNECTED = 1;
    }
}
