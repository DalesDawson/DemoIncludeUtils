package com.zhengquan.zqlibrary.util;

import android.util.Log;

/**
 * Created by zhengquan on 2017/11/15/015.
 */

public class LogUtils {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static int level = VERBOSE;
    public static final String TAG = "LogUtils";

    /**
     * @param tag log标签
     * @param msg log内容
     */
    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    /**
     * @param tag log标签
     * @param msg log内容
     */
    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.v(tag, msg);
        }
    }

    /**
     * @param tag log标签
     * @param msg log内容
     */
    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.v(tag, msg);
        }
    }

    /**
     * @param tag log标签
     * @param msg log内容
     */
    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.v(tag, msg);
        }
    }

    /**
     * @param tag log标签
     * @param msg log内容
     */
    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.v(tag, msg);
        }
    }
}
