package com.vidswift.downloader.allvideosaver.download;

public class DownloadItem {
    public int id;
    public String fileName;
    public String filePath;
    public String url;
    public long downloadId;

    public DownloadItem(int id, String fileName, String filePath, String url, long downloadId) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.url = url;
        this.downloadId = downloadId;
    }
}