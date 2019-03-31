package com.ooftf.homer.lib;

import android.app.Application;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/3/31 0031
 */
public class Homer {
    private static Application application;
    private static boolean log;
    private static IJson jsonParser;

    public static void init(Application application, boolean isLog, IJson jsonParser) {
        Homer.application = application;
        Homer.log = isLog;
        Homer.jsonParser = jsonParser;
    }

    public static Application getApplication() {
        return application;
    }

    public static boolean isLog() {
        return log;
    }

    public static IJson getJsonParser() {
        return jsonParser;
    }
}
