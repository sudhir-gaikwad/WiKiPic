package com.wikipic.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class VolleyErrorCode {
    // Http error codes
    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_CLIENT_ERROR = 400;
    public static final int HTTP_UNAUTHORIZED_ERROR = 401;
    public static final int HTTP_FORBIDDEN_ERROR = 403;
    public static final int HTTP_TIME_OUT_ERROR = 408;
    public static final int HTTP_SERVER_ERROR = 500;
    public static final int HTTP_NETWORK_ERROR = 503;
    public static final int HTTP_PARSE_ERROR = 8003;
    public static final int HTTP_NO_CONNECTION_ERROR = 8001;

    public static int getErrorCode(VolleyError error) {
        if (error instanceof NetworkError) {
            return HTTP_NETWORK_ERROR;
        } else if (error instanceof ServerError) {
            return HTTP_SERVER_ERROR;
        } else if (error instanceof AuthFailureError) {
            return HTTP_UNAUTHORIZED_ERROR;
        } else if (error instanceof ParseError) {
            return HTTP_PARSE_ERROR;
        } else if (error instanceof NoConnectionError) {
            return HTTP_NO_CONNECTION_ERROR;
        } else if (error instanceof TimeoutError) {
            return HTTP_TIME_OUT_ERROR;
        }
        return 0;
    }
}
