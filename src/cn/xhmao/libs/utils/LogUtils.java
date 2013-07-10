package cn.xhmao.libs.utils;

import android.util.Log;

import cn.xhmao.libs.BuildConfig;

/**
 * Created by xhmao on 5/31/13.
 */
public class LogUtils {
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg);
    }
}
