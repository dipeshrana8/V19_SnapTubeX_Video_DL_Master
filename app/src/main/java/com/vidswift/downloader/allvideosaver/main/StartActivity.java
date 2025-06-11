package com.vidswift.downloader.allvideosaver.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.vidswift.downloader.allvideosaver.databinding.ActivitySplashBinding;
import com.vidswift.downloader.allvideosaver.earn.Preference;


public class StartActivity extends BaseActivity {

    ActivitySplashBinding binding;
    private boolean IntroChecked = false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showNativeAds = false;
        showInterstitialAds = true;
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("spin_dialog_shown", false).apply();

        sharedPreferences = getSharedPreferences("FfPref", MODE_PRIVATE);
        IntroChecked = sharedPreferences.getBoolean("IntroChecked", false);

        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        String oneSignalAppId = Preference.getOneSignalAppId();
        if (!oneSignalAppId.isEmpty()) {
            OneSignal.initWithContext(this, oneSignalAppId);

            OneSignal.getNotifications().requestPermission(false, Continue.none());
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (IntroChecked) {

                startActivity(new Intent(StartActivity.this, GroundActivity.class));
                finish();
            } else {

                startActivity(new Intent(StartActivity.this, OnboardingActivity.class));
                finish();
            }
        }, 3500);
    }
}
