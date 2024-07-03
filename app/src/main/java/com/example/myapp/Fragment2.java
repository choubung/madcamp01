package com.example.myapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;


import androidx.core.content.FileProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Fragment2 extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 200;

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Uri> imageUriList = new ArrayList<>();
    private String currentPhotoPath;

    private static final String SHARED_PREFS_NAME = "image_prefs";
    private static final String KEY_IMAGE_PATHS = "image_paths";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        imageAdapter = new ImageAdapter(imageUriList, uri -> {
            Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(ImageDetailActivity.EXTRA_IMAGE_URI, uri.toString());
            startActivity(intent);
        });
        recyclerView.setAdapter(imageAdapter);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> showImageSourceDialog());

        loadImagesFromStorage();

        // SharedPreferences에 저장된 모든 사진 데이터의 Uri를 로그로 출력
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> imagePaths = sharedPreferences.getStringSet(KEY_IMAGE_PATHS, new HashSet<>());
        for (String path : imagePaths) {
            Log.d("onCreateView - 데이터 확인", "Stored image path: " + path);
        }

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        saveImagePathsToSharedPreferences();
    }

    private void saveImagePathsToSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> imagePaths = new HashSet<>();
        for (Uri uri : imageUriList) {
            imagePaths.add(uri.toString());
        }
        editor.putStringSet(KEY_IMAGE_PATHS, imagePaths);
        editor.apply();
    }

    private void showImageSourceDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("이미지 선택")
                .setItems(new String[]{"사진 가져오기", "사진 촬영하기"}, (dialog, which) -> {
                    if (which == 0) {
                        pickImageFromGallery();
                    } else if (which == 1) {
                        captureImageFromCamera();
                    }
                })
                .show();
    }

    private void pickImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_STORAGE_PERMISSION);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void captureImageFromCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(requireContext(),
                            "com.example.myapp.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImageFromCamera();
            } else {
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(requireContext(), "저장소 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // 이미지 파일을 저장한 후, 리스트에 추가하고 어댑터에 알림
                if (currentPhotoPath != null) {
                    File imageFile = new File(currentPhotoPath);
                    Uri imageUri = Uri.fromFile(imageFile);
                    if (!imageUriList.contains(imageUri)) { // 중복 저장 방지
                        imageUriList.add(imageUri);
                        imageAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireContext(), "이미 추가된 사진입니다.", Toast.LENGTH_SHORT).show();
                    }
                    // 이미지 URI를 SharedPreferences에 저장
                    saveImagePath(imageUri);
                } else {
                    Toast.makeText(requireContext(), "사진을 가져오는 데 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                if (!imageUriList.contains(imageUri)) { // 중복 저장 방지
                    imageUriList.add(imageUri);
                    imageAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "이미 추가된 사진입니다.", Toast.LENGTH_SHORT).show();
                }

                saveImagePath(imageUri);
            }
        }
    }

    private void saveImagePath(Uri uri) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> imagePaths = sharedPreferences.getStringSet(KEY_IMAGE_PATHS, new HashSet<>());
        imagePaths.add(uri.toString()); // 변경 전: uri.toString() -> 변경 후: uri
        editor.putStringSet(KEY_IMAGE_PATHS, imagePaths);
        editor.apply();

        // Log로 저장된 이미지 경로 확인
        Log.d("Fragment2", "Saved image path: " + uri.toString());
    }

    private void loadImagesFromStorage() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> imagePaths = sharedPreferences.getStringSet(KEY_IMAGE_PATHS, new HashSet<>());

        // 이미지 URI 리스트를 초기화하지 않고 기존 데이터를 유지하도록 수정
        for (String path : imagePaths) {
            Uri imageUri = Uri.parse(path); // string을 uri로 변환
            if (!imageUriList.contains(imageUri)) { // 이미 리스트에 포함되지 않은 경우만 가져옴
                imageUriList.add(imageUri);
            }

            // Log로 불러온 이미지 경로 확인
            Log.d("Fragment2", "Loaded image path: " + imageUri.toString());
        }

        // 기존 데이터를 유지하면서 새로운 데이터도 추가할 수 있도록 notifyDataSetChanged() 호출 위치를 수정
        imageAdapter.notifyDataSetChanged();
    }
}