package com.vidswift.downloader.allvideosaver.main;

import android.content.Intent;
import android.os.Bundle;

import com.vidswift.downloader.allvideosaver.databinding.ActivityHasTagBinding;

public class HasTagActivity extends BaseActivity {
    private ActivityHasTagBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHasTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.headerTitle.setText("Hashtags");
        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnHappy.setOnClickListener(v -> openDetails("Happy"));
        binding.btnLove.setOnClickListener(v -> openDetails("Love"));
        binding.btnSad.setOnClickListener(v -> openDetails("Sad"));
        binding.btnFriendship.setOnClickListener(v -> openDetails("Friendship"));
        binding.btnAngry.setOnClickListener(v -> openDetails("Angry"));
        binding.btnMotivation.setOnClickListener(v -> openDetails("Motivation"));
        binding.btnSelfLoved.setOnClickListener(v -> openDetails("Self-Love"));
        binding.btnAesthetic.setOnClickListener(v -> openDetails("Aesthetic"));
        binding.btnAlone.setOnClickListener(v -> openDetails("Alone"));
        binding.btnAttitude.setOnClickListener(v -> openDetails("Attitude"));
    }

    private void openDetails(String category) {
        Intent intent = new Intent(this, HasDetailsActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    public void onBackPressed() {
        myBackActivity();
    }
}