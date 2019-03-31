package com.ooftf.homer.lib;

import java.io.Closeable;

/**
 * @author ooftf
 * @date 2016/2/1
 */
public class IoUtils {

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }

}
