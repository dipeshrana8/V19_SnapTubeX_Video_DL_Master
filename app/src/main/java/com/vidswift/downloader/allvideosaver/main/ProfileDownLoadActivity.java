package com.vidswift.downloader.allvideosaver.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vidswift.downloader.allvideosaver.databinding.ActivityProfileDownLoadBinding;

import java.io.OutputStream;

public class ProfileDownLoadActivity extends BaseActivity {
    private static final int CREATE_FILE_REQUEST_CODE = 1001;
    private int imageResId;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityProfileDownLoadBinding binding = ActivityProfileDownLoadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.headerTitle.setText("Profile Image");
        binding.toolbarLayout.btnBack.setOnClickListener(v -> onBackPressed());

        imageResId = getIntent().getIntExtra("image_res", -1);

        if (imageResId != -1) {
            binding.imgProfile.setImageResource(imageResId);
            imageBitmap = BitmapFactory.decodeResource(getResources(), imageResId);
        }

        binding.btnPreview.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfilePreviewActivity.class);
            intent.putExtra("image_res", imageResId);
            startActivity(intent);
        });

        binding.btnDownload.setOnClickListener(v -> {
            createFile();
        });
    }

    private void createFile() {
        String filename = "galaxy_" + System.currentTimeMillis() + ".png";
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onBackPressed() {
        myBackActivity();
    }
}