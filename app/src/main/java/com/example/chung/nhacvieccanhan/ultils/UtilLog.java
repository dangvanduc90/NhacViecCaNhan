package com.example.chung.nhacvieccanhan.ultils;

import android.util.Log;

/**
 * Created by toannt on 15/11/2017.
 */

public class UtilLog {

    public static final boolean IS_LOG_SHOW = true;
    public static final String TAG = UtilLog.class.getSimpleName();

    public static void log_d(String tag, String content) {
        if (IS_LOG_SHOW) {
            Log.d(TAG, tag + "\n" + content);
        }
    }

    public static void log_e(String tag, String content) {
        if (IS_LOG_SHOW) {
            Log.e(TAG, tag + "\n" + content);
        }
    }

    public static void log_w(String tag, String content) {
        if (IS_LOG_SHOW) {
            Log.w(TAG, tag + "\n" + content);
        }
    }

    public static void log_v(String tag, String content) {
        if (IS_LOG_SHOW) {
            Log.v(TAG, tag + "\n" + content);
        }
    }

    public static void log_i(String tag, String content) {
        if (IS_LOG_SHOW) {
            Log.i(TAG, tag + "\n" + content);
        }
    }

    public static void log_wtf(String tag, String content) {
        if (IS_LOG_SHOW) {
            Log.wtf(TAG, tag + "\n" + content);
        }
    }

}
