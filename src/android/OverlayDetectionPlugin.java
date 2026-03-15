package com.overlay.security;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import org.apache.cordova.*;
import org.json.JSONArray;

import java.util.List;

public class OverlayDetectionPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (action.equals("isOverlayEnabled")) {

            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Context context = cordova.getActivity().getApplicationContext();
                        boolean overlayDetected = isAnotherAppOverlaying(context);
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

    private boolean isAnotherAppOverlaying(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // Get all apps that have SYSTEM_ALERT_WINDOW permission active
                AccessibilityManager am = (AccessibilityManager)
                        context.getSystemService(Context.ACCESSIBILITY_SERVICE);

                if (am != null) {
                    List<AccessibilityServiceInfo> services =
                            am.getEnabledAccessibilityServiceList(
                                    AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

                    // Check running overlay-capable apps via Settings
                    String enabledServices = Settings.Secure.getString(
                            context.getContentResolver(),
                            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

                    // Primary check: any app has draw over other apps permission
                    // excluding the system itself
                    String packageName = context.getPackageName();

                    // Check via AppOps if any non-system app can draw overlays
                    android.app.AppOpsManager appOps =
                            (android.app.AppOpsManager) context.getSystemService(
                                    Context.APP_OPS_SERVICE);

                    if (appOps != null) {
                        android.content.pm.PackageManager pm = context.getPackageManager();
                        List<android.content.pm.ApplicationInfo> apps =
                                pm.getInstalledApplications(
                                        android.content.pm.PackageManager.GET_META_DATA);

                        for (android.content.pm.ApplicationInfo app : apps) {
                            // Skip system apps and our own app
                            if (app.packageName.equals(packageName)) continue;
                            if ((app.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0)
                                continue;

                            try {
                                int mode = appOps.checkOpNoThrow(
                                        android.app.AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
                                        app.uid,
                                        app.packageName);

                                if (mode == android.app.AppOpsManager.MODE_ALLOWED) {
                                    return true; // Another app has overlay permission active
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
