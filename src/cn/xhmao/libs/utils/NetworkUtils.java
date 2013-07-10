package cn.xhmao.libs.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.xhmao.libs.BuildConfig;

/**
 * Created by xhmao on 7/5/13.
 */
public class NetworkUtils {
    private final static String TAG = NetworkUtils.class.getSimpleName();

    public static boolean isConnect(Context context) {
        ConnectivityManager c = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (c != null) {
                NetworkInfo info = c.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    return true;
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                LogUtils.i(TAG, "isConnect() exception");
            }
        }

        return false;
    }
}
