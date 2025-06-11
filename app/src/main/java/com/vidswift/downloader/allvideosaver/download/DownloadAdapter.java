package com.vidswift.downloader.allvideosaver.download;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.vidswift.downloader.allvideosaver.R;
import com.vidswift.downloader.allvideosaver.databinding.ItemDownloadBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private final Context context;
    private final OnItemClickListener playListener;
    private final OnMoreClickListener moreListener;
    private final List<DownloadItem> completedDownloads = new ArrayList<>();


    public DownloadAdapter(
            Context context,
            OnItemClickListener playListener,
            OnMoreClickListener moreListener
    ) {
        this.context = context;
        this.playListener = playListener;
        this.moreListener = moreListener;
    }

    public void setDownloads(List<DownloadItem> downloads) {
        completedDownloads.clear();
        for (DownloadItem item : downloads) {
            if (item.filePath != null) {
                completedDownloads.add(item);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDownloadBinding binding = ItemDownloadBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new DownloadViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        DownloadItem item = completedDownloads.get(position);

        if (item == null || item.filePath == null) return; // safety check

        holder.binding.textFileName.setText(item.fileName);
        holder.binding.textFileName.setSelected(true);
        holder.binding.textDetails.setSelected(true);

        try {
            File videoFile = new File(item.filePath);
            if (!videoFile.exists()) {
                throw new Exception("File not found");
            }

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(item.filePath);

            Bitmap thumbnail = retriever.getFrameAtTime();
            if (thumbnail != null) {
                holder.binding.thumbnail.setImageBitmap(thumbnail);
            } else {
                holder.binding.thumbnail.setImageResource(R.mipmap.ic_launcher);
            }

            String durationStr = retriever.extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_DURATION
            );
            if (durationStr != null) {
                long durationMs = Long.parseLong(durationStr);
                String formattedDuration = String.format(
                        "%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(durationMs),
                        TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
                );
                holder.binding.textDuration.setText(formattedDuration);
            } else {
                holder.binding.textDuration.setText("0:00");
            }

            long fileSize = videoFile.length();
            String sizeText = String.format("%.2f MB", fileSize / (1024.0 * 1024.0));
            holder.binding.textDetails.setText(sizeText);

            retriever.release();
        } catch (Exception e) {
            holder.binding.thumbnail.setImageResource(R.mipmap.ic_launcher);
            holder.binding.textDuration.setText("N/A");
            holder.binding.textDetails.setText("File error");

        }

        View.OnClickListener playClick = v -> playListener.onItemClick(item.filePath);
        holder.binding.play.setOnClickListener(playClick);
        holder.binding.getRoot().setOnClickListener(playClick);

        holder.binding.btnMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.getMenuInflater().inflate(R.menu.menu_download_more, popup.getMenu());
            popup.setOnMenuItemClickListener(menuItem -> {
                int id = menuItem.getItemId();

                if (id == R.id.menu_share) {

                    File file = new File(item.filePath);
                    if (file.exists()) {
                        Uri fileUri = FileProvider.getUriForFile(
                                context,
                                context.getPackageName() + ".provider",
                                file
                        );
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(
                                Intent.createChooser(shareIntent, "Share Video")
                        );
                    } else {
                        Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (id == R.id.menu_delete) {

                    boolean deleted = false;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            ContentResolver resolver = context.getContentResolver();
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
                                    long mediaId = cursor.getLong(
                                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                                    );
                                    Uri deleteUri = ContentUris.withAppendedId(
                                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                            mediaId
                                    );
                                    deleted = resolver.delete(deleteUri, null, null) > 0;
                                }
                            }
                        } else {
                            File f = new File(item.filePath);
                            if (f.exists()) {
                                deleted = f.delete();
                            }
                        }
                    } catch (Exception e) {

                    }

                    if (deleted) {
                        Toast.makeText(context, "Video deleted", Toast.LENGTH_SHORT).show();
                        moreListener.onMoreClick(item);
                    } else {
                        Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return completedDownloads.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String filePath);
    }

    public interface OnMoreClickListener {
        void onMoreClick(DownloadItem downloadItem);
    }

    static class DownloadViewHolder extends RecyclerView.ViewHolder {
        final ItemDownloadBinding binding;

        DownloadViewHolder(ItemDownloadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}