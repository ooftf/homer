package com.ooftf.homer.lib;

import android.util.Log;

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/3/31 0031
 */
public class HomerLog {
    public static void log(String tag, String message) {
        if (Homer.isLog()) {
            Log.d(tag, message);
        }
    }
}
