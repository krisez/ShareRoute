package cn.krisez.shareroute.utils;

import android.util.Log;

public class LogUtils {
    private static final String TAG = "SR";
    private static boolean log = false;

    public static void isLog(boolean logging) {
        log = logging;
    }

    public static void e(String content) {
        if (log) {
            Log.e(TAG, "e: " + content);
        }
    }
}
