[![](https://jitpack.io/v/ooftf/homer.svg)](https://jitpack.io/#ooftf/homer)
## 使用方式
1. 引入AutoRegister库 https://github.com/luckybilly/AutoRegister
2. autoregister配置
```gradle
autoregister {
    registerInfo = [
            [
                    'scanInterface'          : 'com.ooftf.homer.lib.IpcHandler'
                    , 'codeInsertToClassName': 'com.ooftf.homer.lib.IpcPathManager'
                    //未指定codeInsertToMethodName，默认插入到static块中，故此处register必须为static方法
                    , 'registerMethodName'   : 'register' //
            ],
            [
                    'scanInterface'          : 'com.ooftf.homer.lib.IClient',
                    'scanSuperClasses'       : ['com.ooftf.homer.lib.AbsIpcClient']
                    , 'codeInsertToClassName': 'com.ooftf.homer.lib.IpcHostManager'
                    //未指定codeInsertToMethodName，默认插入到static块中，故此处register必须为static方法
                    , 'registerMethodName'   : 'register' //
            ]

    ]
}
```
3. 添加IpcHandler实例用于处理跨进程请求
4. 添加AbsIpcClient用于向不同进程发送请求，内置向主进程发起通讯请求的Client为IpcMainClient，host地址为IpcConst.MAIN_HOST（main.IpcMainClient.IpcMainService）
5. 初始化
```
 Homer.init(this, true, new IJson() {
            @Override
            public <T> T fromJson(String json, Class<T> clz) {
                return JSON.parseObject(json, clz);
            }

            @Override
            public <T> T fromJson(String json, Type type) {
                return JSON.parseObject(json, type);
            }

            @Override
            public String toJson(Object object) {
                return JSON.toJSONString(object);
            }
        });
```
5. 发起请求代码
```java
IpcClient.request(IpcClient.getBaseUri(IpcConst.MAIN_HOST).path("/first").build()).subscribe(new Consumer<IpcResponseBody>() {
                    @Override
                    public void accept(IpcResponseBody ipcResponseBody) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
```
## 添加指定进程Host
1. 添加Android组件Service 继承 IpcService 指定进程  例如IpcOtherService
2. 添加client
```

@IpcUriHost("other.IpcOtherClient.IpcOtherService")
public class IpcOtherClient extends AbsIpcClient {
    @Override
    protected Class getServiceClass() {
        return IpcOtherService.class;
    }
}
```
