package com.vidswift.downloader.allvideosaver.spalsh;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;

import com.vidswift.downloader.allvideosaver.R;
import com.vidswift.downloader.allvideosaver.databinding.ActivityProfilePreviewBinding;

public class ProfilePreviewActivity extends BaseActivity {
    ActivityProfilePreviewBinding binding;
    private int imageResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityProfilePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.headerTitle.setText("Preview");
        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());
        imageResId = getIntent().getIntExtra("image_res", -1);

        if (imageResId != -1) {
            binding.imgInstaPre1.setImageResource(imageResId);
            binding.imgInstaPre2.setImageResource(imageResId);
            binding.imgYouPre1.setImageResource(imageResId);
            binding.imgYouPre2.setImageResource(imageResId);
            binding.imgTwitterPre1.setImageResource(imageResId);
            binding.imgTwitterPre2.setImageResource(imageResId);
        }
        binding.btnInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgButton.setImageResource(R.drawable.img_instagram_selected);
                binding.lnInsta.setVisibility(VISIBLE);
                binding.lnYou.setVisibility(GONE);
                binding.lnTwitter.setVisibility(GONE);
            }
        });
        binding.btnUtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgButton.setImageResource(R.drawable.img_youtube_selected);
                binding.lnInsta.setVisibility(GONE);
                binding.lnYou.setVisibility(VISIBLE);
                binding.lnTwitter.setVisibility(GONE);
            }
        });

        binding.btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgButton.setImageResource(R.drawable.img_twitter_selected);
                binding.lnInsta.setVisibility(GONE);
                binding.lnYou.setVisibility(GONE);
                binding.lnTwitter.setVisibility(VISIBLE);
            }
        });
    }

    public void onBackPressed() {
        myBackActivity();
    }
}