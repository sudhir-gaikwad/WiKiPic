package com.wikipic.controller;

import android.content.Context;

import com.wikipic.util.LogUtil;

import java.util.concurrent.ConcurrentHashMap;

public class ControllerManager {

    private static final String TAG = ControllerManager.class.getSimpleName();

    private static volatile ControllerManager sControllerInstance = null;
    private Context mContext = null;
    private SearchController mSearchController = null;
    private NetworkManager mNetworkManager = null;
    private static int sRequestId = 0;
    private ConcurrentHashMap<Integer, ControllerCallback> mListenersMap =
            new ConcurrentHashMap<Integer, ControllerCallback>();

    public static ControllerManager getInstance() {
        return sControllerInstance;
    }

    public static ControllerManager createInstance(Context context) {
        if (sControllerInstance == null) {              // Single Checked
            synchronized (ControllerManager.class) {
                if (sControllerInstance == null) {      // Double checked
                    sControllerInstance = new ControllerManager(context);
                }
            }
        }
        return sControllerInstance;
    }

    private ControllerManager(Context context) {
        mContext = context;

        mNetworkManager = new NetworkManager(mContext);
        mSearchController = new SearchController(this);
    }

    public NetworkManager getNetworkManager() {
        return mNetworkManager;
    }

    public SearchController getSearchController() {
        return mSearchController;
    }

    public synchronized int getNextRequestId() {
        if (sRequestId == Integer.MAX_VALUE) {
            sRequestId = 0;
        }

        return ++sRequestId;
    }

    /**
     * Adds a listener to the {@link #mListenersMap} so that callback can be
     * sent properly to the right listener.
     *
     * @param requestId against which listener needs to be added into the
     *                  {@link #mListenersMap}.
     * @param listener  which needs to be added into the {@link #mListenersMap}.
     */
    public void addListener(int requestId, ControllerCallback listener) {
        if (listener == null) {
            throw new NullPointerException("Listener should not be null");
        }
        if ((mListenersMap != null) && (mListenersMap.get(requestId) != null)) {
            mListenersMap.remove(requestId);
        }
        mListenersMap.put(requestId, listener);
    }

    /**
     * Removes a listener to the {@link #mListenersMap} so that callback will
     * not be recieved by listener as now he don't wants to.
     *
     * @param requestId against which listener needs to be removed from the
     *                  {@link #mListenersMap}.
     */
    public void removeListener(int requestId) {
        if (mListenersMap != null && mListenersMap.get(requestId) != null) {
            mListenersMap.remove(requestId);
            LogUtil.w(TAG, "Cancelling Request [" + requestId + "]" +
                    " result = " + (mListenersMap.get(requestId) == null));
        }
    }

    public ControllerCallback getControllerCallback(int requestId) {
        ControllerCallback callback = null;
        if (mListenersMap != null) {
            callback = mListenersMap.get(requestId);
        }
        // LOG should be shown if ControllerCallback is Null
        if (callback == null) {
            LogUtil.e(TAG, "ControllerCallback is null");
        }
        return callback;
    }

    public void cancelRequest(int requestId) {
        removeListener(requestId);
        mNetworkManager.cancelPendingRequests(requestId);
    }
}
