package com.ooftf.homer.sample;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.ooftf.homer.lib.Homer;
import com.ooftf.homer.lib.IJson;

import java.lang.reflect.Type;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
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
    }
}
