package com.wikipic.com.wikipic.request;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.InputStreamRequest;
import com.wikipic.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class ImageRequest extends InputStreamRequest {

    public ImageRequest(int method, String url, Listener<byte[]> listener,
                        ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Constants.HTTP_CONSTANTS.HEADER_ACCEPT_LANGUAGE_KEY,
                Constants.HTTP_CONSTANTS.HEADER_ACCEPT_LANGUAGE_VALUE);
        return headers;
    }
}
