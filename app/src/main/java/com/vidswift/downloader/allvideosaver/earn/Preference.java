package com.vidswift.downloader.allvideosaver.earn;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Preference {

    public static final String Native_OnOff = "native_show";
    public static final String Interstitial_OnOff = "interstial_onoff";
    public static final String REDIRECT_LINK_KEY = "redirect_link";
    private static final String STRING_LIST_PREF = "string_list_pref";
    private static final String INTERSTITIAL_KEY = "interstitial";
    private static final String NATIVE_LINK_KEY = "native_link";

    private static final String PRIVACY_POLICY_KEY = "privacy_policy_link";
    private static final String ONESIGNAL_APP_ID_KEY = "onesignal_id";

    private static SharedPreferences preference() {
        return MyController.get().getSharedPreferences("adController", Context.MODE_PRIVATE);
    }

    public static String getRedirectLink() {
        return preference().getString(REDIRECT_LINK_KEY, "");
    }

    public static void setRedirectLink(String value) {
        preference().edit().putString(REDIRECT_LINK_KEY, value).apply();
    }

    public static void setappopen_redirect(String value) {
        preference().edit().putString(INTERSTITIAL_KEY, value).apply();
    }

    public static String getappopen_redirect() {
        return preference().getString(INTERSTITIAL_KEY, "");
    }

    public static boolean getNative_show() {
        return preference().getBoolean(Native_OnOff, false);
    }

    public static void setNative_show(boolean value) {
        preference().edit().putBoolean(Native_OnOff, value).apply();
    }

    public static void setnative_link(String value) {
        preference().edit().putString(NATIVE_LINK_KEY, value).apply();
    }

    public static String getnative_redirect_link() {
        return preference().getString(NATIVE_LINK_KEY, "");
    }

    public static boolean getIninterstialWeb() {
        return preference().getBoolean(Interstitial_OnOff, false);
    }

    public static void setIninterstialWeb(boolean value) {
        preference().edit().putBoolean(Interstitial_OnOff, value).apply();
    }

    public static ArrayList<String> getStringListPref() {
        String json = preference().getString(STRING_LIST_PREF, null);
        if (json != null) {
            try {
                return new Gson().fromJson(json, new TypeToken<ArrayList<String>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public static void setStringListPref(ArrayList<String> list) {
        String json = new Gson().toJson(list);
        preference().edit().putString(STRING_LIST_PREF, json).apply();
    }

    public static String getPrivacyPolicyUrl() {
        return preference().getString(PRIVACY_POLICY_KEY, "");
    }

    public static void setPrivacyPolicyUrl(String url) {
        preference().edit().putString(PRIVACY_POLICY_KEY, url).apply();
    }

    public static String getOneSignalAppId() {
        return preference().getString(ONESIGNAL_APP_ID_KEY, "");
    }

    public static void setOneSignalAppId(String appId) {
        preference().edit().putString(ONESIGNAL_APP_ID_KEY, appId).apply();
    }
}

