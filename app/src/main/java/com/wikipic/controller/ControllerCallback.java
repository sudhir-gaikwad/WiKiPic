package com.wikipic.controller;

import android.app.Activity;
import android.os.Handler;

import com.wikipic.util.LogUtil;

import java.lang.ref.WeakReference;

public abstract class ControllerCallback {

    private static final String TAG = ControllerCallback.class.getSimpleName();
    private WeakReference<Activity> mActivityReference = null;
    private WeakReference<Handler> mHandler = null;

    public ControllerCallback(Activity activity) {
        if (activity == null) {
            throw new NullPointerException();
        }
        mActivityReference = new WeakReference<Activity>(activity);
    }

    public ControllerCallback(Handler handler) {
        if (handler == null) {
            throw new NullPointerException();
        }
        mHandler = new WeakReference<Handler>(handler);
    }

    protected abstract void onSuccessResponse(int requestId, int status, Object data);

    protected abstract void onErrorResponse(int requestId, int status, Object data);

    public void onSuccessResponseProxy(final int requestId, final int status, final Object data) {
        LogUtil.d(TAG, "onSuccessResponseProxy Called");
        // Weak Reference NULL is Included to avoid Null Pointer Crash
        if (null != mActivityReference && null != mActivityReference.get()) {
            Activity activity = mActivityReference.get();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    onSuccessResponse(requestId, status, data);
                }
            });
            activity = null;
            // Weak Reference NULL is Included to avoid Null Pointer Crash
        } else if (null != mHandler && null != mHandler.get()) {
            Handler handler = mHandler.get();
            handler.post(new Runnable() {
                public void run() {
                    onSuccessResponse(requestId, status, data);
                }
            });
            handler = null;
        } else {
            LogUtil.e(TAG, "Listner Object Reference is NULL now in SuccessResponse" +
                    " (Assuming Activity is destroyed)");
        }
    }

    public void onErrorResponseProxy(final int requestId, final int status, final Object data) {
        LogUtil.d(TAG, "onErrorResponseProxy Called");
        // Weak Reference NULL is Included to avoid Null Pointer Crash
        if (null != mActivityReference && null != mActivityReference.get()) {
            Activity activity = mActivityReference.get();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    onErrorResponse(requestId, status, data);
                }
            });
            activity = null;
            // Weak Reference NULL is Included to avoid Null Pointer Crash
        } else if (null != mHandler && null != mHandler.get()) {
            Handler handler = mHandler.get();
            handler.post(new Runnable() {
                public void run() {
                    onErrorResponse(requestId, status, data);
                }
            });
            handler = null;
        } else {
            LogUtil.e(TAG, "Listner Object Reference is NULL now in ErrorResponse" +
                    " (Assuming Activity is destroyed)");
        }
    }
}
