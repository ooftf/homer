package com.ooftf.homer.lib.sp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class SPProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Context context = getContext();
        String type = uri.getQueryParameter("type").toLowerCase();
        String key = uri.getQueryParameter("key");
        String name = uri.getQueryParameter("name");
        String defaultValue = uri.getQueryParameter("defaultValue");
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"value"});
        Object value = null;
        switch (type) {
            case "string":
                value = context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defaultValue);
                break;
            case "int":
                int defaultInt = 0;
                try {
                    defaultInt = Integer.valueOf(defaultValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                value = context.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defaultInt);
                break;
            case "float":
                float defaultFloat = 0;
                try {
                    defaultFloat = Float.valueOf(defaultValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                value = context.getSharedPreferences(name, Context.MODE_PRIVATE).getFloat(key, defaultFloat);
                break;
            case "boolean":
                boolean defaultBoolean = false;
                try {
                    defaultBoolean = Boolean.valueOf(defaultValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                value = context.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defaultBoolean);
                break;
            case "long":
                long defaultLong = 0;
                try {
                    defaultLong = Long.valueOf(defaultValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                value = context.getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defaultLong);
                break;
            default:
                break;
        }
        if (value != null) {
            matrixCursor.addRow(new Object[]{value});
        }
        return matrixCursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Context context = getContext();
        String key = uri.getQueryParameter("key");
        String name = uri.getQueryParameter("name");
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).apply();
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Context context = getContext();
        String type = uri.getQueryParameter("type").toLowerCase();
        String key = uri.getQueryParameter("key");
        String name = uri.getQueryParameter("name");
        String value = uri.getQueryParameter("value");
        switch (type) {
            case "string":
                context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).apply();
                break;
            case "int":
                int defaultInt = 0;
                try {
                    defaultInt = Integer.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
                context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, defaultInt).apply();
                break;
            case "float":
                float defaultFloat = 0;
                try {
                    defaultFloat = Float.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
                context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putFloat(key, defaultFloat).apply();
                break;
            case "boolean":
                boolean defaultBoolean = false;
                try {
                    defaultBoolean = Boolean.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
                context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, defaultBoolean).apply();
                break;
            case "long":
                long defaultLong = 0;
                try {
                    defaultLong = Long.valueOf(value);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
                context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putLong(key, defaultLong).apply();
                break;
            default:
                break;
        }
        return 0;
    }
}
