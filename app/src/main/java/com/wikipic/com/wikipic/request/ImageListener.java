package com.wikipic.com.wikipic.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wikipic.controller.ControllerCallback;
import com.wikipic.model.Images;
import com.wikipic.controller.ControllerManager;
import com.wikipic.util.LogUtil;
import com.wikipic.util.VolleyErrorCode;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class ImageListener implements Response.Listener<byte[]>, Response.ErrorListener {

    public int mRequestId;

    public ImageListener(int requestId) {
        mRequestId = requestId;
    }

    @Override
    public void onResponse(byte[] response) {
        Images images = null;
        try {
            InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(response));
            images = new Gson().fromJson(reader, Images.class);
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
            images = new Images();
            images.setErrorDescription("Failed to parse network response");
        }

        ControllerManager controllerManager = ControllerManager.getInstance();
        ControllerCallback controllerCallback = controllerManager.getControllerCallback(mRequestId);
        // Send callback to all the listeners.
        if (controllerCallback != null) {
            if (!images.getErrorDescription().equalsIgnoreCase("")) {
                controllerCallback.onErrorResponseProxy(mRequestId, 1, images);
            } else {
                controllerCallback.onSuccessResponseProxy(mRequestId, 0, images);
            }
            controllerManager.removeListener(mRequestId);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ControllerManager controllerManager = ControllerManager.getInstance();
        ControllerCallback controllerCallback = controllerManager.getControllerCallback(mRequestId);
        Images images = new Images();
        images.setErrorCode(Integer.toString(VolleyErrorCode.getErrorCode(error)));
        images.setErrorDescription(error.getMessage());

        // Send callback to all the listeners.
        if (controllerCallback != null) {
            controllerCallback.onErrorResponseProxy(mRequestId, 0, images);
            controllerManager.removeListener(mRequestId);
        }
    }
}
