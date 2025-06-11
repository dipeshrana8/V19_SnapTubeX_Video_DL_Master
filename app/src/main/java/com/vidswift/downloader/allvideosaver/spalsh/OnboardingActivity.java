package com.vidswift.downloader.allvideosaver.spalsh;

import android.content.Intent;
import android.os.Bundle;

import com.vidswift.downloader.allvideosaver.databinding.ActivityOnboardingBinding;

public class OnboardingActivity extends BaseActivity {

    ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnNext.setOnClickListener(v -> {

            Intent intent = new Intent(OnboardingActivity.this, LanguageActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onBackPressed() {
        myBackActivity();
    }
}