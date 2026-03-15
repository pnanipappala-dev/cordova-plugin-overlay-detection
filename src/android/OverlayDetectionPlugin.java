package com.overlay.security;

import android.view.MotionEvent;
import org.apache.cordova.*;
import org.json.JSONArray;

public class OverlayDetectionPlugin extends CordovaPlugin {

    private static boolean overlayDetected = false;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (action.equals("isOverlayEnabled")) {
            callbackContext.success(overlayDetected ? 1 : 0);
            return true;
        }

        return false;
    }

    @Override
    public boolean onDispatchTouchEvent(MotionEvent event) {

        if ((event.getFlags() & MotionEvent.FLAG_WINDOW_IS_OBSCURED) != 0 ||
            (event.getFlags() & MotionEvent.FLAG_WINDOW_IS_PARTIALLY_OBSCURED) != 0) {

            overlayDetected = true;

        } else {

            overlayDetected = false;
        }

        return false;
    }
}
