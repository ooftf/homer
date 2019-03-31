package com.ooftf.homer.lib;

import java.lang.reflect.Type;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/3/31 0031
 */
public interface IJson {
    <T> T fromJson(String json, Class<T> clz);

    <T> T fromJson(String json, Type type);

    String toJson(Object object);


}
