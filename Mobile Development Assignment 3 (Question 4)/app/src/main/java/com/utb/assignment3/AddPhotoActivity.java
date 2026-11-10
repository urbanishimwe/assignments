package com.utb.assignment3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class AddPhotoActivity extends AppCompatActivity {
    private static final String TAG = "AddPhotoActivity";

    private ImageButton btnBackAdd;
    private ImageView imgAddPreview;
    private Button btnChooseGallery;
    private Button btnTakePhoto;
    private EditText editAddTitle;
    private EditText editAddDescription;
    private Button btnSavePhoto;

    private String selectedImageUri = "";

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        selectedImageUri = PhotoStorageManager.saveImageToInternalStorage(this, uri);
                        imgAddPreview.setImageURI(Uri.parse(selectedImageUri));
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.get("data") instanceof Bitmap) {
                        Bitmap photoBitmap = (Bitmap) extras.get("data");
                        selectedImageUri = saveBitmapToInternalStorage(photoBitmap);
                        imgAddPreview.setImageBitmap(photoBitmap);
                    }
                }
            });

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required to take photos.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        initViews();
        setupListeners();

        selectedImageUri = "android.resource://" + getPackageName() + "/" + R.drawable.sample_nature;
    }

    private void initViews() {
        btnBackAdd = findViewById(R.id.btnBackAdd);
        imgAddPreview = findViewById(R.id.imgAddPreview);
        btnChooseGallery = findViewById(R.id.btnChooseGallery);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        editAddTitle = findViewById(R.id.editAddTitle);
        editAddDescription = findViewById(R.id.editAddDescription);
        btnSavePhoto = findViewById(R.id.btnSavePhoto);
    }

    private void setupListeners() {
        btnBackAdd.setOnClickListener(v -> finish());

        btnChooseGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndLaunch());

        btnSavePhoto.setOnClickListener(v -> savePhoto());
    }

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    private void savePhoto() {
        String title = editAddTitle.getText() != null ? editAddTitle.getText().toString().trim() : "";
        String description = editAddDescription.getText() != null ? editAddDescription.getText().toString().trim() : "";

        if (title.isEmpty()) {
            Toast.makeText(this, R.string.err_no_title, Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null || selectedImageUri.isEmpty()) {
            selectedImageUri = "android.resource://" + getPackageName() + "/" + R.drawable.sample_nature;
        }

        Photo newPhoto = new Photo(
                UUID.randomUUID().toString(),
                title,
                description,
                selectedImageUri,
                false,
                System.currentTimeMillis()
        );

        PhotoStorageManager.addPhoto(this, newPhoto);
        Toast.makeText(this, "Photo added successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String saveBitmapToInternalStorage(Bitmap bitmap) {
        try {
            File photosDir = new File(getFilesDir(), "user_photos");
            if (!photosDir.exists()) {
                if (!photosDir.mkdirs()) {
                    Log.w(TAG, "Failed to create directory or directory exists");
                }
            }
            File destFile = new File(photosDir, "camera_" + System.currentTimeMillis() + ".jpg");
            try (FileOutputStream out = new FileOutputStream(destFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            }
            return Uri.fromFile(destFile).toString();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save camera bitmap", e);
            return selectedImageUri;
        }
    }
}
