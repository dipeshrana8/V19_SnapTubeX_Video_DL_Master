package com.vidswift.downloader.allvideosaver.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import com.vidswift.downloader.allvideosaver.R;
import com.vidswift.downloader.allvideosaver.databinding.ActivitySettingBinding;

public class PrivatesActivity extends BaseActivity {
    boolean wifiOnly;
    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.headerTitle.setText("Setting");
        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());
        SharedPreferences prefs = getSharedPreferences("app_settings", MODE_PRIVATE);
        wifiOnly = prefs.getBoolean("wifi_only", false);


        String selectedLanguage = prefs.getString("selected_language", "Not selected");
        binding.txtLang.setText(selectedLanguage);
        binding.txtPath.setSelected(true);
        binding.imgWifi.setImageResource(wifiOnly ? R.drawable.img_wifi_only_on : R.drawable.img_wifi_only_off);

        String downloadPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        binding.txtPath.setText("Path: " + downloadPath);

        binding.btnWifi.setOnClickListener(v -> {
            wifiOnly = !wifiOnly;
            prefs.edit().putBoolean("wifi_only", wifiOnly).apply();
            binding.imgWifi.setImageResource(wifiOnly ? R.drawable.img_wifi_only_on : R.drawable.img_wifi_only_off);
        });

        binding.btnHowToDownload.setOnClickListener(v -> {
            Intent intent = new Intent(this, How1Activity.class);
            startActivity(intent);
        });

        binding.btnRate.setOnClickListener(v -> {
            rateApp();
        });
        binding.btnShare.setOnClickListener(v -> {
            shareApp();
        });
        binding.btnPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(this, PolicyActivity.class);
            startActivity(intent);
        });
    }

    public void onBackPressed() {
        myBackActivity();
    }
}