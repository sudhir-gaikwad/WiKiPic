package com.wikipic;

import android.app.Application;

import com.wikipic.controller.ControllerManager;

public class WiKiPic extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize all controllers and network manager.
        ControllerManager.createInstance(getApplicationContext());
    }
}
