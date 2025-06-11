package com.vidswift.downloader.allvideosaver.download;

import static android.view.View.GONE;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vidswift.downloader.allvideosaver.R;
import com.vidswift.downloader.allvideosaver.databinding.ItemProcessBinding;

import java.util.List;


public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.ProcessViewHolder> {
    private final List<ProcessItem> processes;
    private final OnPlayClickListener playListener;
    private final OnCancelClickListener cancelListener;

    public ProcessAdapter(List<ProcessItem> processes, OnPlayClickListener playListener, OnCancelClickListener cancelListener) {
        this.processes = processes;
        this.playListener = playListener;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public ProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProcessBinding binding = ItemProcessBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProcessViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, int position) {
        ProcessItem item = processes.get(position);
        holder.binding.textFileName.setText(item.fileName);
        holder.binding.textFileName.setSelected(true);
        holder.binding.progressBar.setProgress(item.progress);
        holder.binding.textProgress.setText(item.status.equals("Failed") ? "Failed" : item.progress + "%");
        holder.binding.thumbnail.setImageResource(R.mipmap.ic_launcher); // Placeholder thumbnail
        holder.binding.textDuration.setText("N/A");
        holder.binding.play.setOnClickListener(v -> playListener.onPlayClick());
        holder.binding.play.setVisibility(GONE);
        holder.binding.btnMore.setOnClickListener(v -> cancelListener.onCancelClick(item.downloadId));
    }

    @Override
    public int getItemCount() {
        return processes.size();
    }

    public interface OnPlayClickListener {
        void onPlayClick();
    }

    public interface OnCancelClickListener {
        void onCancelClick(long downloadId);
    }

    static class ProcessViewHolder extends RecyclerView.ViewHolder {
        ItemProcessBinding binding;

        ProcessViewHolder(ItemProcessBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}