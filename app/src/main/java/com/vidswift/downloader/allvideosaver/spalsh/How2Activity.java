package com.vidswift.downloader.allvideosaver.spalsh;

import android.content.Intent;
import android.os.Bundle;

import com.vidswift.downloader.allvideosaver.databinding.ActivityHow2Binding;

public class How2Activity extends BaseActivity {

    ActivityHow2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHow2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.btnBack.setOnClickListener(v -> myBackActivity());
        binding.toolbarLayout.headerTitle.setText("How To Use");

        binding.btnNext.setOnClickListener(v -> {

            Intent intent = new Intent(How2Activity.this, How3Activity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onBackPressed() {
        myBackActivity();
    }
}