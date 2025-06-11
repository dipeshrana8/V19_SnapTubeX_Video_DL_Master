package com.vidswift.downloader.allvideosaver.download;

import androidx.room.PrimaryKey;


public class ProcessItem {
    @PrimaryKey(autoGenerate = true)

    public long downloadId;
    public String fileName;
    public String url;
    public int progress;
    public String status;

    public ProcessItem(long downloadId, String fileName, String url, int progress, String status) {
        this.downloadId = downloadId;
        this.fileName = fileName;
        this.url = url;
        this.progress = progress;
        this.status = status;
    }
}