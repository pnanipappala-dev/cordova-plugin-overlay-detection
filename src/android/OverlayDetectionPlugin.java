package com.overlay.security;

import android.provider.Settings;
import android.os.Build;
import android.content.Context;

import org.apache.cordova.*;
import org.json.JSONArray;

public class OverlayDetectionPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (action.equals("isOverlayEnabled")) {

            Context context = cordova.getActivity().getApplicationContext();
            boolean overlayDetected = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                overlayDetected = Settings.canDrawOverlays(context);
            }

            callbackContext.success(overlayDetected ? 1 : 0);
            return true;
        }

        return false;
    }
}
