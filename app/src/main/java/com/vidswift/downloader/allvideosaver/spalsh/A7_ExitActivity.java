package com.vidswift.downloader.allvideosaver.spalsh;

import android.os.Bundle;

import com.vidswift.downloader.allvideosaver.databinding.ActivityExitBinding;

public class A7_ExitActivity extends BaseActivity {

    ActivityExitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolbarHeaderText = "Are Sure to Exit?";
        showSettings = true;
        super.onCreate(savedInstanceState);
        binding = ActivityExitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(
                binding.toolbarLayout.headerTitle,
                binding.toolbarLayout.btnBack,
                binding.toolbarLayout.btnSettings
        );
        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());
        setupToolbar(binding.toolbarLayout.headerTitle, binding.toolbarLayout.btnBack, binding.toolbarLayout.btnSettings);

        binding.btnYes.setOnClickListener(v -> {
            finishAffinity();
        });


        binding.btnNo.setOnClickListener(v -> {

            myBackActivity();
        });

    }

    @Override
    public void onBackPressed() {
        myBackActivity();
    }
}