package com.vidswift.downloader.allvideosaver.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vidswift.downloader.allvideosaver.databinding.ActivityHow3Binding;

public class How3Activity extends BaseActivity {
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    ActivityHow3Binding binding;
    private int permissionDenialCount = 0;


    private String[] permissionsToRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHow3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarLayout.btnBack.setOnClickListener(v -> myBackActivity());
        binding.toolbarLayout.headerTitle.setText("How To Use");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest = new String[]{Manifest.permission.READ_MEDIA_VIDEO};
        } else {
            permissionsToRequest = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }


        binding.btnNext.setOnClickListener(v -> {
            if (hasPermissions()) {

                Intent intent = new Intent(How3Activity.this, GroundActivity.class);
                startActivity(intent);
            } else {

                Toast.makeText(this, "This task needs permission", Toast.LENGTH_SHORT).show();
                showRationaleDialog(permissionDenialCount);
            }
        });
    }


    private boolean hasPermissions() {
        for (String permission : permissionsToRequest) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private void showRationaleDialog(int denialCount) {
        String message = denialCount == 0
                ? "This app needs storage permissions to work. Please allow them."
                : "We really need these permissions to continue. Please grant them.";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions Needed");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {

            ActivityCompat.requestPermissions(How3Activity.this, permissionsToRequest, REQUEST_CODE_PERMISSIONS);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Permissions");
        builder.setMessage("You’ve denied permissions. Please enable them in the app settings.");
        builder.setPositiveButton("Go to Settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (arePermissionsGranted(grantResults)) {

                Intent intent = new Intent(How3Activity.this, GroundActivity.class);
                startActivity(intent);
            } else {

                permissionDenialCount++;
                if (permissionDenialCount < 2 && ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsToRequest[0])) {

                    showRationaleDialog(permissionDenialCount);
                } else {

                    showSettingsDialog();
                }
            }
        }
    }


    private boolean arePermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        myBackActivity();
    }
}