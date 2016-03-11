package com.wikipic.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    /**
     * Check if network is connected to internet
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = false;
        if (activeNetwork != null && activeNetwork.isConnected()) {
            isConnected = true;
        }
        return isConnected;
    }
}
