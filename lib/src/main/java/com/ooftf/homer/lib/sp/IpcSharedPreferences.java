package com.ooftf.homer.lib.sp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ooftf.homer.lib.Homer;
import com.ooftf.homer.lib.ProcessUtils;

public class IpcSharedPreferences {
    public static boolean getBoolean(String name, String key, boolean defaultValue) {
        if (ProcessUtils.isMainProcess()) {
            return Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
        }
        try {
            return Boolean.valueOf(getValue(name, "boolean", key, String.valueOf(defaultValue)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }


    public static String getString(String name, String key, String defaultValue) {
        if (ProcessUtils.isMainProcess()) {
            return Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defaultValue);
        }
        try {
            return getValue(name, "string", key, String.valueOf(defaultValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static int getInt(String name, String key, int defaultValue) {
        if (ProcessUtils.isMainProcess()) {
            return Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defaultValue);
        }
        try {
            return Integer.valueOf(getValue(name, "int", key, String.valueOf(defaultValue)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static long getLong(String name, String key, long defaultValue) {
        if (ProcessUtils.isMainProcess()) {
            return Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defaultValue);
        }
        try {
            return Long.valueOf(getValue(name, "long", key, String.valueOf(defaultValue)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static float getFloat(String name, String key, float defaultValue) {
        if (ProcessUtils.isMainProcess()) {
            return Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).getFloat(key, defaultValue);
        }
        try {
            return Float.valueOf(getValue(name, "float", key, String.valueOf(defaultValue)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    private static String getValue(String name, String type, String key, String defaultValue) {
        Uri uri = new Uri.Builder().scheme("content")
                .authority("com.ooftf.homer")
                .appendQueryParameter("type", type)
                .appendQueryParameter("key", key)
                .appendQueryParameter("name", name)
                .appendQueryParameter("defaultValue", defaultValue)
                .build();
        Cursor cursor1 = Homer.getApplication().getContentResolver().query(uri, null, null, null, null);
        if (cursor1 != null) {
            boolean has = cursor1.moveToFirst();
            if (has) {
                String value = cursor1.getString(cursor1.getColumnIndex("value"));
                cursor1.close();
                return value;
            } else {
                cursor1.close();
            }
        }
        return "";
    }


    public static void putBoolean(String name, String key, boolean value) {
        if (ProcessUtils.isMainProcess()) {
            Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
        }
        try {
            getValue(name, "boolean", key, String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void putString(String name, String key, String value) {
        if (ProcessUtils.isMainProcess()) {
            Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).apply();
        }
        try {
            getValue(name, "string", key, String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putInt(String name, String key, int value) {
        if (ProcessUtils.isMainProcess()) {
            Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
        }
        try {
            getValue(name, "int", key, String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putLong(String name, String key, long value) {
        if (ProcessUtils.isMainProcess()) {
            Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putLong(key, value).apply();
        }
        try {
            getValue(name, "long", key, String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void putFloat(String name, String key, float value) {
        if (ProcessUtils.isMainProcess()) {
            Homer.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE).edit().putFloat(key, value).apply();
        }
        try {
            putValue(name, "float", key, String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void putValue(String name, String type, String key, String value) {
        Uri uri = new Uri.Builder().scheme("content")
                .authority("com.ooftf.homer")
                .appendQueryParameter("type", type)
                .appendQueryParameter("key", key)
                .appendQueryParameter("name", name)
                .appendQueryParameter("value", value)
                .build();
        Homer.getApplication().getContentResolver().update(uri, null, null, null);
    }

    private static void remove(String name, String key) {
        Uri uri = new Uri.Builder().scheme("content")
                .authority("com.ooftf.homer")
                .appendQueryParameter("key", key)
                .appendQueryParameter("name", name)
                .build();
        Homer.getApplication().getContentResolver().delete(uri, null, null);
    }
}
