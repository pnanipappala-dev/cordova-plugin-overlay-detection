package com.overlay.security;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.MotionEvent;
import android.app.ActivityManager;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class OverlayDetectionPlugin extends CordovaPlugin {

    private boolean touchObscured = false;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (action.equals("isOverlayEnabled")) {
            try {
                Context context = cordova.getActivity().getApplicationContext();

                // Check 1: Does any app have SYSTEM_ALERT_WINDOW permission?
                boolean canDrawOverlays = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    canDrawOverlays = !Settings.canDrawOverlays(context);
                    // canDrawOverlays(context) returns true for YOUR app
                    // We want to know if OTHER apps have it — this is a general flag
                }

                // Check 2: Was touch obscured?
                boolean overlayDetected = touchObscured || canDrawOverlays;

                JSONObject result = new JSONObject();
                result.put("isOverlayed", overlayDetected);
                result.put("touchObscured", touchObscured);
                result.put("canDrawOverlays", canDrawOverlays);

                callbackContext.success(result);
                return true;

            } catch (Exception e) {
                callbackContext.error("Error checking overlay: " + e.getMessage());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onDispatchTouchEvent(MotionEvent event) {
        if ((event.getFlags() & MotionEvent.FLAG_WINDOW_IS_OBSCURED) != 0 ||
            (event.getFlags() & MotionEvent.FLAG_WINDOW_IS_PARTIALLY_OBSCURED) != 0) {
            touchObscured = true; // ← Set true, never reset to false automatically
        }
        return false;
    }
}
