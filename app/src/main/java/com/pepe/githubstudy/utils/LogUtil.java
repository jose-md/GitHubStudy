package com.pepe.githubstudy.utils;

import android.util.Log;

import com.pepe.githubstudy.BuildConfig;


/**
 * @author 1one
 * @date 2019/7/29.
 */
public class LogUtil {
    public static final String TAG = "GitHub";
    public static boolean isDebug = BuildConfig.DEBUG;

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, getClassName() + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, getClassName() + msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, getClassName() + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, getClassName() + msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, getClassName() + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, getClassName() + msg);
        }
    }

    private static String getClassName() {
        String result;
        // 这里的数组的index2是根据你工具类的层级做不同的定义，这里仅仅是关键代码
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        return result + "   ";
    }
}
