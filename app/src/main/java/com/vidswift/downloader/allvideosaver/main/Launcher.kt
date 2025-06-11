package com.vidswift.downloader.allvideosaver.main

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.vidswift.downloader.allvideosaver.R
import com.vidswift.downloader.allvideosaver.earn.Preference
import com.vidswift.downloader.allvideosaver.earn.MyController
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class Launcher : MyController() {

    private var editor: SharedPreferences.Editor? = null

    companion object {
        lateinit var sharedPreferences: SharedPreferences
    }

    private val databaseName = "ad_demo"

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        sharedPreferences = getSharedPreferences(databaseName, MODE_PRIVATE)
        editor = sharedPreferences.edit()

        setupFirebaseRemoteConfig()
    }

    private fun setupFirebaseRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(100)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                try {
                    val jsonString = remoteConfig.getString("comgetfreediamonds")


                    val jsonObject = JSONObject(jsonString)

                    Preference.setNative_show(
                        jsonObject.optBoolean(
                            Preference.Native_OnOff, false
                        )
                    )
                    Preference.setIninterstialWeb(
                        jsonObject.optBoolean(
                            "interstial_onoff",
                            false
                        )
                    )

                    val bannerImageArray = extractStringList(jsonObject, "daily_BannerImage")
                    Preference.setStringListPref(bannerImageArray)

                    val multipleLinkArray = extractStringList(jsonObject, "multiple_link")
                    if (multipleLinkArray.isNotEmpty()) {
                        val selectedLink = multipleLinkArray.random()
                        Preference.setRedirectLink(selectedLink)
                        Preference.setnative_link(selectedLink)
                        Preference.setappopen_redirect(selectedLink)
                    }

                    val privacyPolicyUrl = jsonObject.optString("privacy_policy_link", "")
                    Preference.setPrivacyPolicyUrl(privacyPolicyUrl)

                    val oneSignalAppId = jsonObject.optString("onesignal_id", "")
                    Preference.setOneSignalAppId(oneSignalAppId)


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun extractStringList(jsonObject: JSONObject, key: String): ArrayList<String> {
        val result = ArrayList<String>()
        try {
            val jsonArray = JSONArray(jsonObject.getString(key))
            for (i in 0 until jsonArray.length()) {
                result.add(jsonArray.getString(i))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }
}
