package com.vidswift.downloader.allvideosaver.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class DownloadReceiver extends BroadcastReceiver {
    private static final String TAG = "DownloadReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == -1) return;

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager == null) return;

            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
            try (Cursor cursor = manager.query(query)) {
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                        if (filePath != null && filePath.startsWith("file://")) {
                            filePath = filePath.substring(7); // Convert URI to path
                        }
                        Intent localIntent = new Intent("download_complete");
                        localIntent.putExtra("downloadId", downloadId);
                        localIntent.putExtra("filePath", filePath);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}