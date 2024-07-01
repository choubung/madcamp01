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

public class ImageDetailActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_URI = "com.example.myapp.EXTRA_IMAGE_URI";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView imageView = findViewById(R.id.imageViewDetail);

        String imageUriString = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        if (imageUriString != null) {
            try {
                // 이미지 URI를 Bitmap으로 변환
                Bitmap bitmap = decodeBitmapFromFileUri(imageUriString);

                // 이미지 크기를 적절히 조정
                int maxDimension = 1024; // 최대 너비 또는 높이
                bitmap = resizeBitmap(bitmap, maxDimension);

                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap decodeBitmapFromFileUri(String imageUriString) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(Uri.parse(imageUriString));
        return BitmapFactory.decodeStream(inputStream);
    }

    private Bitmap resizeBitmap(Bitmap originalBitmap, int maxSize) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        float aspectRatio = (float) width / height;
        int newWidth = maxSize;
        int newHeight = Math.round(newWidth / aspectRatio);

        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
    }
}