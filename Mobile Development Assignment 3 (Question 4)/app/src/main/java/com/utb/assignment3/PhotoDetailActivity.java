package com.utb.assignment3;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetailActivity";

    private ImageButton btnBackDetail;
    private ImageView imgDetailPhoto;
    private TextView txtDetailTitle;
    private ImageButton btnDetailFavorite;
    private TextView txtDetailDate;
    private TextView txtDetailDescription;
    private Button btnDeletePhoto;

    private Photo currentPhoto;
    private String photoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        photoId = getIntent().getStringExtra("PHOTO_ID");
        if (photoId == null) {
            finish();
            return;
        }

        initViews();
        loadPhotoData();
        setupListeners();
    }

    private void initViews() {
        btnBackDetail = findViewById(R.id.btnBackDetail);
        imgDetailPhoto = findViewById(R.id.imgDetailPhoto);
        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        btnDetailFavorite = findViewById(R.id.btnDetailFavorite);
        txtDetailDate = findViewById(R.id.txtDetailDate);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);
        btnDeletePhoto = findViewById(R.id.btnDeletePhoto);
    }

    private void loadPhotoData() {
        List<Photo> photos = PhotoStorageManager.getPhotos(this);
        for (Photo p : photos) {
            if (p.getId().equals(photoId)) {
                currentPhoto = p;
                break;
            }
        }

        if (currentPhoto == null) {
            finish();
            return;
        }

        txtDetailTitle.setText(currentPhoto.getTitle());
        txtDetailDescription.setText(currentPhoto.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
        String formattedDate = sdf.format(new Date(currentPhoto.getTimestamp()));
        txtDetailDate.setText(getString(R.string.date_added, formattedDate));

        updateFavoriteIcon();
        loadPhotoUri(imgDetailPhoto, currentPhoto.getImageUri());
    }

    private void updateFavoriteIcon() {
        if (currentPhoto.isFavorite()) {
            btnDetailFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            btnDetailFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void loadPhotoUri(ImageView imageView, String uriString) {
        if (uriString == null || uriString.isEmpty()) {
            imageView.setImageResource(R.drawable.sample_nature);
            return;
        }

        try {
            Uri uri = Uri.parse(uriString);
            imageView.setImageURI(uri);
        } catch (Exception e) {
            Log.e(TAG, "Error loading photo image URI: " + uriString, e);
            imageView.setImageResource(R.drawable.sample_nature);
        }
    }

    private void setupListeners() {
        btnBackDetail.setOnClickListener(v -> finish());

        btnDetailFavorite.setOnClickListener(v -> {
            if (currentPhoto != null) {
                currentPhoto.setFavorite(!currentPhoto.isFavorite());
                PhotoStorageManager.updatePhoto(this, currentPhoto);
                updateFavoriteIcon();
            }
        });

        btnDeletePhoto.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_msg)
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                    PhotoStorageManager.deletePhoto(PhotoDetailActivity.this, photoId);
                    Toast.makeText(PhotoDetailActivity.this, "Photo deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }
}
