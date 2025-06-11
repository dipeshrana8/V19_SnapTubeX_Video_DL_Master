package com.vidswift.downloader.allvideosaver.main;

import android.content.Intent;
import android.os.Bundle;

import com.vidswift.downloader.allvideosaver.databinding.ActivityHow1Binding;

public class How1Activity extends BaseActivity {

    ActivityHow1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHow1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbarLayout.btnBack.setOnClickListener(v -> myBackActivity());
        binding.toolbarLayout.headerTitle.setText("How To Use");
        binding.btnNext.setOnClickListener(v -> {

            Intent intent = new Intent(How1Activity.this, How2Activity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onBackPressed() {
        myBackActivity();
    }
}