package com.example.myapp;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

import android.net.Uri;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<Uri> imageUriList;
    private OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(Uri uri);
    }

    public ImageAdapter(List<Uri> imageUriList, OnImageClickListener listener) {
        this.imageUriList = imageUriList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUri = imageUriList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(imageUri)
                .apply(new RequestOptions().transform(new CircleCrop()))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> listener.onImageClick(imageUri));
    }

    @Override
    public int getItemCount() {
        return imageUriList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}