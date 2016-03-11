
package com.wikipic.util;

import android.text.TextUtils;
import android.util.Log;

public class LogUtil {
    private static boolean DEBUG = true;
    private static final String TAG = "WiKiPic";

    /**
     * Log error.
     * @param tag - The tag to use for this logging event.
     * @param msg - The message to print out for this logging event.
     */
    public static void e(String tag, String msg) {
        if (DEBUG && msg != null) {
            Log.e(TAG, "[" + tag + "] " + msg);
        }
    }

    /**
     * Log warning.
     * @param tag - The tag to use for this logging event.
     * @param msg - The message to print out for this logging event.
     */
    public static void w(String tag, String msg) {
        if (DEBUG && msg != null) {
            Log.w(TAG, "[" + tag + "] " + msg);
        }
    }

    /**
     * Log debug.
     * @param tag - The tag to use for this logging event.
     * @param msg - The message to print out for this logging event.
     * @param tr  - The exception to throw.
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG && msg != null) {
            Log.d(TAG, "[" + tag + "] " + msg, tr);
        }
    }

    /**
     * Log Error.
     * @param tag - The tag to use for this logging event.
     * @param msg - The message to print out for this logging event.
     * @param tr  - The exception to throw.
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (DEBUG && msg != null) {
            Log.e(TAG, "[" + tag + "] " + msg, tr);
        }
    }

    /**
     * Log debug.
     * @param tag - The tag to use for this logging event.
     * @param msg - The message to print out for this logging event.
     */
    public static void d(String tag, String msg) {
        if (DEBUG && msg != null) {
            Log.d(TAG, "[" + tag + "] " + msg);
        }
    }

    /**
     * Log Info.
     * @param tag - The tag to use for this logging event.
     * @param msg - The message to print out for this logging event.
     */
    public static void i(String tag, String msg) {
        if (DEBUG && msg != null) {
            Log.i(TAG, "[" + tag + "] " + msg);
        }
    }

    /**
     * Log verbose.
     * @param tag - The tag to use for this logging event.
     * @param msg - The message to print out for this logging event.
     */
    public static void v(String tag, String msg) {
        if (DEBUG && msg != null) {
            Log.v(TAG, "[" + tag + "] " + msg);
        }
    }

    /**
     * Print the stack trace.
     * @param e - The exception to use for this stack trace.
     */
    public static void printStackTrace(Exception e) {
        if (DEBUG && e != null) {
            if (!TextUtils.isEmpty(e.getMessage())) {
                Log.e(TAG, e.getMessage());
            }
            e.printStackTrace();
        }
    }
}
