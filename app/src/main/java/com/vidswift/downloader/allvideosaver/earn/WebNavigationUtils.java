package com.vidswift.downloader.allvideosaver.earn;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.vidswift.downloader.allvideosaver.R;


public class WebNavigationUtils {


    public static void WebInterstitial(Context context) {
        if (context == null) return;

        if (!Preference.getIninterstialWeb()) return;

        String redirectUrl = Preference.getRedirectLink();
        if (redirectUrl == null || redirectUrl.isEmpty()) return;

        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

            CustomTabColorSchemeParams colorParams = new CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(context, R.color.colorprimary))
                    .build();
            builder.setDefaultColorSchemeParams(colorParams);

            builder.setShowTitle(true);
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON);
            builder.setInstantAppsEnabled(true);

            CustomTabsIntent customTabsIntent = builder.build();

            if (isChromeInstalled(context)) {
                customTabsIntent.intent.setPackage("com.android.chrome");
            } else {

                return;
            }

            Uri uri = Uri.parse(redirectUrl);
            customTabsIntent.launchUrl(context, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static boolean isChromeInstalled(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo("com.android.chrome", 0);
            return ai.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}