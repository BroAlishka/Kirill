package com.example.homework9.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.homework9.R;
import com.example.homework9.model.ImageItem;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<ImageItem> imageItems;

    public ImageAdapter(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageItem item = imageItems.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
        holder.previewImageView.setContentDescription(
                holder.itemView.getContext().getString(R.string.image_content_description, item.getTitle())
        );

        Glide.with(holder.previewImageView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_error)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.previewImageView);
    }

    @Override
    public void onViewRecycled(@NonNull ImageViewHolder holder) {
        Glide.with(holder.previewImageView.getContext()).clear(holder.previewImageView);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView previewImageView;
        private final TextView titleTextView;
        private final TextView descriptionTextView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            previewImageView = itemView.findViewById(R.id.previewImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
