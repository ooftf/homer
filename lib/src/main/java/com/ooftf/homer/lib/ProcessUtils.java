package com.ooftf.homer.lib;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.support.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author 99474
 */
public class ProcessUtils {
    public static boolean isMainProcess() {
        return Homer.getApplication().getPackageName().equals(getCurrentProcessName());
    }

    /**
     * Return the value of current process.
     *
     * @return the value of current process
     */
    @Nullable
    public static String getCurrentProcessName() {
        try {
            return getProcessName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getProcessName2();
    }

    private static String getProcessName2() {
        try {
            ActivityManager am = (ActivityManager) Homer.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
            if (info != null && info.size() > 0) {
                int pid = Process.myPid();
                for (ActivityManager.RunningAppProcessInfo aInfo : info) {
                    if (aInfo.pid == pid) {
                        if (aInfo.processName != null) {
                            return aInfo.processName;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Maximum length allowed in {@code /proc/self/cmdline}.  Imposed to avoid a large buffer
     * allocation during the init value.
     */
    private static final int CMDLINE_BUFFER_SIZE = 64;

    private static String sProcessName;
    private static boolean sProcessNameRead;

    /**
     * Get process value by reading {@code /proc/self/cmdline}.
     *
     * @return Process value or null if there was an error reading from {@code /proc/self/cmdline}.
     * It is unknown how this error can occur in practice and should be considered extremely
     * rare.
     */
    @Nullable
    private static synchronized String getProcessName() {
        if (!sProcessNameRead) {
            sProcessNameRead = true;
            try {
                sProcessName = readProcessName();
            } catch (IOException e) {
            }
        }
        return sProcessName;
    }

    private static String readProcessName() throws IOException {
        byte[] cmdlineBuffer = new byte[CMDLINE_BUFFER_SIZE];

        // Avoid using a Reader to not pick up a forced 16K buffer.  Silly java.io...
        FileInputStream stream = new FileInputStream("/proc/self/cmdline");
        try {
            int n = stream.read(cmdlineBuffer);
            int endIndex = indexOf(cmdlineBuffer, 0, n, (byte) 0 );
            return new String(cmdlineBuffer, 0, endIndex > 0 ? endIndex : n);
        } finally {
            IoUtils.closeSilently(stream);
        }
    }

    private static int indexOf(byte[] haystack, int offset, int length, byte needle) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isWebProcess() {
        String currentProcessName = getCurrentProcessName();
        if (currentProcessName != null && currentProcessName.equals(Homer.getApplication().getPackageName() + ":web")) {
            return true;
        }
        return false;
    }

    public static boolean isJdPushProcess() {
        String currentProcessName = getCurrentProcessName();
        if (currentProcessName != null && currentProcessName.equals(Homer.getApplication().getPackageName() + ":jdpush")) {
            return true;
        }
        return false;
    }


}
