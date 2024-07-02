package com.example.myapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImageDetailActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_URI = "com.example.myapp.EXTRA_IMAGE_URI";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView imageView = findViewById(R.id.imageViewDetail);

        String imageUriString = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);

            Glide.with(this)
                    .load(imageUri)
                    .into(imageView);
        }
    }
}