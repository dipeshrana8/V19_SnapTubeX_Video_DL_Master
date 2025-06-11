package com.vidswift.downloader.allvideosaver.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vidswift.downloader.allvideosaver.R;
import com.vidswift.downloader.allvideosaver.databinding.ActivityHomeBinding;
import com.vidswift.downloader.allvideosaver.download.DownloadAdapter;
import com.vidswift.downloader.allvideosaver.download.DownloadItem;
import com.vidswift.downloader.allvideosaver.download.ProcessAdapter;
import com.vidswift.downloader.allvideosaver.download.ProcessItem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


public class GroundActivity extends BaseActivity {
    private static final String DOWNLOAD_SUBDIR = "VideoDownloader";
    private static final int REQUEST_CODE_PERMISSIONS = 100;

    private ActivityHomeBinding binding;
    private DownloadAdapter downloadAdapter;
    private ProcessAdapter processAdapter;
    private final List<DownloadItem> downloadItems = new ArrayList<>();
    private final List<ProcessItem> processItems = new ArrayList<>();
    private final BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra("downloadId", -1);
            String fileUri = intent.getStringExtra("fileUri");
            if (downloadId != -1 && fileUri != null) {
                onDownloadComplete(downloadId, fileUri);
            }
        }
    };
    private Handler handler;
    private Runnable updateRunnable;
    private int permissionDenialCount = 0;
    private String[] permissionsToRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest = new String[]{Manifest.permission.READ_MEDIA_VIDEO};
        } else {
            permissionsToRequest = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }

        binding.headerTitle.setText("Video Downloader");
        binding.btnBack.setVisibility(View.GONE);
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        setupRecyclerViews();

        if (hasPermissions()) {
            loadExistingVideos();
        }

        startProgressUpdates();
        updateVisibility();

        binding.btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(GroundActivity.this, PrivatesActivity.class);
            startActivity(intent);
        });

        binding.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroundActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.btnHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroundActivity.this, HasTagActivity.class);
                startActivity(intent);
            }
        });
        binding.btnHome.setOnClickListener(v -> {
            binding.headerTitle.setText("Video Downloader");
            binding.imgButton.setImageResource(R.drawable.img_home_seletct);
            binding.lnHome.setVisibility(View.VISIBLE);
            binding.lnlnDownload.setVisibility(View.GONE);
            binding.lnDownloadProcess.setVisibility(View.GONE);
            binding.btnBack.setVisibility(View.GONE);
            updateVisibility();
        });

        binding.btnProcess.setOnClickListener(v -> {
            binding.headerTitle.setText("Downloading Process");
            binding.imgButton.setImageResource(R.drawable.img_process_seletct);
            binding.lnDownloadProcess.setVisibility(View.VISIBLE);
            binding.btnBack.setVisibility(View.VISIBLE);
            binding.lnlnDownload.setVisibility(View.GONE);
            binding.lnHome.setVisibility(View.GONE);
            updateVisibility();
        });

        binding.btnMine.setOnClickListener(v -> {
            binding.headerTitle.setText("Download Video");
            binding.imgButton.setImageResource(R.drawable.img_mine_select);
            binding.lnHome.setVisibility(View.GONE);
            binding.btnBack.setVisibility(View.VISIBLE);
            binding.lnlnDownload.setVisibility(View.VISIBLE);
            binding.lnDownloadProcess.setVisibility(View.GONE);
            updateVisibility();
            if (hasPermissions()) {
                loadExistingVideos();
            }
        });

        binding.btnDownload.setOnClickListener(v -> {
            String url = binding.editTextUrl.getText().toString().trim();
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidUrl(url)) {
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
                return;
            }
            if (hasPermissions()) {
                startDownload(url);
            } else {
                Toast.makeText(this, "This task needs permission", Toast.LENGTH_SHORT).show();
                showRationaleDialog(permissionDenialCount);
            }
        });
    }

    private void loadExistingVideos() {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File videoDir = new File(downloadsDir, DOWNLOAD_SUBDIR);
        if (videoDir.exists() && videoDir.isDirectory()) {
            File[] files = videoDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".mp4");
                }
            });
            if (files != null) {
                for (File file : files) {
                    String filePath = file.getAbsolutePath();
                    boolean exists = false;
                    for (DownloadItem item : downloadItems) {
                        if (item.filePath != null && item.filePath.equals(filePath)) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        String fileName = file.getName();
                        DownloadItem item = new DownloadItem(0, fileName, filePath, "", -1);
                        downloadItems.add(item);
                    }
                }
            }
        }
        downloadAdapter.setDownloads(downloadItems);
        updateVisibility();
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
        String message = (denialCount == 0)
                ? "This app needs storage permissions to work. Please allow them."
                : "We really need these permissions to continue. Please grant them.";

        new AlertDialog.Builder(this)
                .setTitle("Permissions Needed")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    ActivityCompat.requestPermissions(
                            GroundActivity.this,
                            permissionsToRequest,
                            REQUEST_CODE_PERMISSIONS
                    );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Enable Permissions")
                .setMessage("You’ve denied permissions. Please enable them in the app settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (arePermissionsGranted(grantResults)) {
                Toast.makeText(this, "Permissions granted, you can now download videos", Toast.LENGTH_SHORT).show();
                loadExistingVideos();
            } else {
                permissionDenialCount++;
                if (permissionDenialCount < 2 &&
                        ActivityCompat.shouldShowRequestPermissionRationale(
                                this, permissionsToRequest[0]
                        )) {
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
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                downloadCompleteReceiver,
                new IntentFilter("download_complete")
        );
        updateVisibility();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadCompleteReceiver);
    }


    private void setupRecyclerViews() {
        binding.downloadRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        downloadAdapter = new DownloadAdapter(
                this,
                filePath -> {
                    try {
                        Uri videoUri = Uri.parse(filePath);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(videoUri, "video/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(this, "Unable to play video", Toast.LENGTH_SHORT).show();
                    }
                },
                this::deleteVideo
        );
        downloadAdapter.setDownloads(downloadItems);
        binding.downloadRecyclerView.setAdapter(downloadAdapter);

        binding.processRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        processAdapter = new ProcessAdapter(
                processItems,
                () -> Toast.makeText(this, "Download in progress", Toast.LENGTH_SHORT).show(),
                downloadId -> cancelDownload(downloadId)
        );
        binding.processRecyclerView.setAdapter(processAdapter);
    }

    private void startProgressUpdates() {
        handler = new Handler(Looper.getMainLooper());
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateProgress();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateRunnable);
    }

    private void updateProgress() {
        if (processItems.isEmpty()) {
            Log.d("Process", "No items in processItems");
            updateVisibility();
            return;
        }

        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (manager == null) return;

        Log.d("Process", "Updating progress for " + processItems.size() + " items");
        for (ProcessItem item : processItems) {
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(item.downloadId);
            try (Cursor cursor = manager.query(query)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int bytesDownloaded = cursor.getInt(
                            cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    );
                    int bytesTotal = cursor.getInt(
                            cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    );
                    int status = cursor.getInt(
                            cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
                    );

                    if (bytesTotal > 0) {
                        int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                        item.progress = progress;
                        item.status = status == DownloadManager.STATUS_RUNNING ? "Downloading"
                                : status == DownloadManager.STATUS_FAILED ? "Failed"
                                : status == DownloadManager.STATUS_SUCCESSFUL ? "Completed"
                                : "Pending";
                    }
                }
            } catch (Exception e) {
                Log.e("Process", "Error updating progress: " + e.getMessage());
                item.status = "Error";
            }
        }
        processAdapter.notifyDataSetChanged();
        updateVisibility();
    }

    private void startDownload(String url) {
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (manager == null) {
            Toast.makeText(this, "Download service unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = "video_" + System.currentTimeMillis() + ".mp4";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading video")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        DOWNLOAD_SUBDIR + File.separator + fileName
                )
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        long downloadId = manager.enqueue(request);

        ProcessItem processItem = new ProcessItem(downloadId, fileName, url, 0, "Pending");
        processItems.add(processItem);
        Log.d("Process", "Added process item, size: " + processItems.size());

        DownloadItem downloadItem = new DownloadItem(0, fileName, null, url, downloadId);
        downloadItems.add(downloadItem);

        processAdapter.notifyDataSetChanged();
        downloadAdapter.setDownloads(downloadItems);
        updateVisibility();

        binding.btnProcess.performClick();
        Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
    }

    private void cancelDownload(long downloadId) {
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.remove(downloadId);
        }
        for (Iterator<ProcessItem> it = processItems.iterator(); it.hasNext(); ) {
            ProcessItem pi = it.next();
            if (pi.downloadId == downloadId) {
                it.remove();
                break;
            }
        }
        for (Iterator<DownloadItem> it = downloadItems.iterator(); it.hasNext(); ) {
            DownloadItem di = it.next();
            if (di.downloadId == downloadId && di.filePath == null) {
                it.remove();
                break;
            }
        }
        processAdapter.notifyDataSetChanged();
        downloadAdapter.setDownloads(downloadItems);
        updateVisibility();
        Toast.makeText(this, "Download canceled", Toast.LENGTH_SHORT).show();
    }

    private void onDownloadComplete(long downloadId, String fileUri) {
        for (Iterator<ProcessItem> it = processItems.iterator(); it.hasNext(); ) {
            ProcessItem pi = it.next();
            if (pi.downloadId == downloadId) {
                it.remove();
                break;
            }
        }
        for (DownloadItem di : downloadItems) {
            if (di.downloadId == downloadId) {
                di.filePath = fileUri;
                break;
            }
        }
        runOnUiThread(() -> {
            processAdapter.notifyDataSetChanged();
            downloadAdapter.setDownloads(downloadItems);
            downloadAdapter.notifyDataSetChanged();
            updateVisibility();
            binding.btnMine.performClick(); // Auto-switch to Download Video section
            Toast.makeText(this, "Download successful", Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteVideo(DownloadItem item) {
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (manager != null && item.downloadId != -1) {
            manager.remove(item.downloadId);
        }

        try {
            if (item.filePath != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = getContentResolver();
                    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String selection = MediaStore.Video.Media.DATA + "=?";
                    String[] selArgs = new String[]{item.filePath};

                    try (Cursor cursor = resolver.query(
                            uri,
                            new String[]{MediaStore.Video.Media._ID},
                            selection,
                            selArgs,
                            null
                    )) {
                        if (cursor != null && cursor.moveToFirst()) {
                            long id = cursor.getLong(
                                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                            );
                            Uri contentUri = ContentUris.withAppendedId(
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    id
                            );
                            resolver.delete(contentUri, null, null);
                        }
                    }
                } else {
                    File file = new File(item.filePath);
                    if (file.exists()) {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            Log.e("Delete", "Failed to delete file: " + item.filePath);
                        }
                    }
                }
            }

            downloadItems.remove(item);
            downloadAdapter.setDownloads(downloadItems);
            updateVisibility();
            Toast.makeText(this, "Video deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Delete", "Error deleting video: " + e.getMessage());
            Toast.makeText(this, "Error deleting video", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateVisibility() {
        if (binding.lnDownloadProcess.getVisibility() == View.VISIBLE) {
            boolean isEmpty = processAdapter.getItemCount() == 0;
            binding.noVideosImage1.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.noVideosImage.setVisibility(View.GONE);
            Log.d("Visibility", "Process section, count: " + processAdapter.getItemCount() + ", noVideosImage1 visible: " + isEmpty);
        } else if (binding.lnlnDownload.getVisibility() == View.VISIBLE) {
            boolean isEmpty = downloadAdapter.getItemCount() == 0;
            binding.noVideosImage.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.noVideosImage1.setVisibility(View.GONE);
            Log.d("Visibility", "Download section, count: " + downloadAdapter.getItemCount() + ", noVideosImage visible: " + isEmpty);
        } else {
            binding.noVideosImage.setVisibility(View.GONE);
            binding.noVideosImage1.setVisibility(View.GONE);
            Log.d("Visibility", "No section active, hiding both noVideosImages");
        }
    }

    private boolean isValidUrl(String url) {
        String urlPattern = "^(http|https)://.*$";
        return Pattern.matches(urlPattern, url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PhoneActivity.class);
        startActivity(intent);
    }
}