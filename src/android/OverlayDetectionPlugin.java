package com.overlay.security;

import android.os.Build;
import android.view.View;
import android.view.Window;

import org.apache.cordova.*;
import org.json.JSONArray;

public class OverlayDetectionPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (action.equals("isOverlayEnabled")) {

            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Window window = cordova.getActivity().getWindow();
                        View decorView = window.getDecorView();

                        boolean overlayDetected = false;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            // Android 10+ — direct API
                            overlayDetected = !decorView.isLaidOut() || decorView.isObscuredCompat();
                        }

                        // Fallback: check FilterTouchesWhenObscured flag
                        boolean filterFlag = (decorView.getFilterTouchesWhenObscured());

                        callbackContext.success(overlayDetected ? 1 : 0);

                    } catch (Exception e) {
                        callbackContext.error("Error: " + e.getMessage());
                    }
                }
            });

            return true;
        }

        return false;
    }
}
