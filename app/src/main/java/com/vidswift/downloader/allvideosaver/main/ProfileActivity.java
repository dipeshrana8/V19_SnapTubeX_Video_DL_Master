package com.vidswift.downloader.allvideosaver.main;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vidswift.downloader.allvideosaver.R;
import com.vidswift.downloader.allvideosaver.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.headerTitle.setText("Profile Image");
        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnAesthetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgButton.setImageResource(R.drawable.img_aesthetic_select);
                binding.frameAesthetic.setVisibility(VISIBLE);
                binding.frameCartoon.setVisibility(GONE);
                binding.frameGalaxy.setVisibility(GONE);
            }
        });
        binding.btnCartoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgButton.setImageResource(R.drawable.img_cartoon_selected);
                binding.frameAesthetic.setVisibility(GONE);
                binding.frameCartoon.setVisibility(VISIBLE);
                binding.frameGalaxy.setVisibility(GONE);
            }
        });

        binding.btnGalaxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imgButton.setImageResource(R.drawable.img_galaxy_selected);
                binding.frameAesthetic.setVisibility(GONE);
                binding.frameCartoon.setVisibility(GONE);
                binding.frameGalaxy.setVisibility(VISIBLE);
            }
        });

        binding.btnAesthetic1.setOnClickListener(v -> openPreview(R.drawable.aesthetic_1));
        binding.btnAesthetic2.setOnClickListener(v -> openPreview(R.drawable.aesthetic_2));
        binding.btnAesthetic3.setOnClickListener(v -> openPreview(R.drawable.aesthetic_3));
        binding.btnAesthetic4.setOnClickListener(v -> openPreview(R.drawable.aesthetic_4));
        binding.btnAesthetic5.setOnClickListener(v -> openPreview(R.drawable.aesthetic_5));
        binding.btnAesthetic6.setOnClickListener(v -> openPreview(R.drawable.aesthetic_6));
        binding.btnAesthetic7.setOnClickListener(v -> openPreview(R.drawable.aesthetic_7));
        binding.btnAesthetic8.setOnClickListener(v -> openPreview(R.drawable.aesthetic_8));
        binding.btnAesthetic9.setOnClickListener(v -> openPreview(R.drawable.aesthetic_9));
        binding.btnAesthetic10.setOnClickListener(v -> openPreview(R.drawable.aesthetic_10));
        binding.btnAesthetic11.setOnClickListener(v -> openPreview(R.drawable.aesthetic_11));
        binding.btnAesthetic12.setOnClickListener(v -> openPreview(R.drawable.aesthetic_12));
        binding.btnAesthetic13.setOnClickListener(v -> openPreview(R.drawable.aesthetic_13));
        binding.btnAesthetic14.setOnClickListener(v -> openPreview(R.drawable.aesthetic_14));

        binding.btnCartoon1.setOnClickListener(v -> openPreview(R.drawable.cartoon_1));
        binding.btnCartoon2.setOnClickListener(v -> openPreview(R.drawable.cartoon_2));
        binding.btnCartoon3.setOnClickListener(v -> openPreview(R.drawable.cartoon_3));
        binding.btnCartoon4.setOnClickListener(v -> openPreview(R.drawable.cartoon_4));
        binding.btnCartoon5.setOnClickListener(v -> openPreview(R.drawable.cartoon_5));
        binding.btnCartoon6.setOnClickListener(v -> openPreview(R.drawable.cartoon_6));
        binding.btnCartoon7.setOnClickListener(v -> openPreview(R.drawable.cartoon_7));
        binding.btnCartoon8.setOnClickListener(v -> openPreview(R.drawable.cartoon_8));


        binding.btnGalaxy1.setOnClickListener(v -> openPreview(R.drawable.galaxy_1));
        binding.btnGalaxy2.setOnClickListener(v -> openPreview(R.drawable.galaxy_2));
        binding.btnGalaxy3.setOnClickListener(v -> openPreview(R.drawable.galaxy_3));
        binding.btnGalaxy4.setOnClickListener(v -> openPreview(R.drawable.galaxy_4));
        binding.btnGalaxy5.setOnClickListener(v -> openPreview(R.drawable.galaxy_5));
        binding.btnGalaxy6.setOnClickListener(v -> openPreview(R.drawable.galaxy_6));
        binding.btnGalaxy7.setOnClickListener(v -> openPreview(R.drawable.galaxy_7));
        binding.btnGalaxy8.setOnClickListener(v -> openPreview(R.drawable.galaxy_8));


    }

    private void openPreview(int imageResId) {
        Intent intent = new Intent(this, ProfileDownLoadActivity.class);
        intent.putExtra("image_res", imageResId);
        startActivity(intent);
    }

    public void onBackPressed() {
        myBackActivity();
    }
}