package com.utb.assignment3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPhotos;
    private EditText editSearch;
    private Button btnFilterAll;
    private Button btnFilterFavorites;
    private Button btnAddPhotoHeader;
    private FloatingActionButton fabAddPhoto;
    private TextView txtEmpty;

    private PhotoAdapter photoAdapter;
    private List<Photo> allPhotos = new ArrayList<>();
    private boolean isFavoritesOnly = false;
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhotos();
    }

    private void initViews() {
        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);
        editSearch = findViewById(R.id.editSearch);
        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterFavorites = findViewById(R.id.btnFilterFavorites);
        btnAddPhotoHeader = findViewById(R.id.btnAddPhotoHeader);
        fabAddPhoto = findViewById(R.id.fabAddPhoto);
        txtEmpty = findViewById(R.id.txtEmpty);

        updateTabButtonStyles();
    }

    private void setupRecyclerView() {
        recyclerViewPhotos.setLayoutManager(new GridLayoutManager(this, 2));
        photoAdapter = new PhotoAdapter(
                this,
                new ArrayList<>(),
                photo -> {
                    Intent intent = new Intent(MainActivity.this, PhotoDetailActivity.class);
                    intent.putExtra("PHOTO_ID", photo.getId());
                    startActivity(intent);
                },
                photo -> {
                    photo.setFavorite(!photo.isFavorite());
                    PhotoStorageManager.updatePhoto(MainActivity.this, photo);
                    loadPhotos();
                }
        );
        recyclerViewPhotos.setAdapter(photoAdapter);
    }

    private void setupListeners() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s != null ? s.toString().trim() : "";
                filterAndDisplayPhotos();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnFilterAll.setOnClickListener(v -> {
            isFavoritesOnly = false;
            updateTabButtonStyles();
            filterAndDisplayPhotos();
        });

        btnFilterFavorites.setOnClickListener(v -> {
            isFavoritesOnly = true;
            updateTabButtonStyles();
            filterAndDisplayPhotos();
        });

        View.OnClickListener openAddListener = v -> {
            Intent intent = new Intent(MainActivity.this, AddPhotoActivity.class);
            startActivity(intent);
        };

        btnAddPhotoHeader.setOnClickListener(openAddListener);
        fabAddPhoto.setOnClickListener(openAddListener);
    }

    private void loadPhotos() {
        allPhotos = PhotoStorageManager.getPhotos(this);
        filterAndDisplayPhotos();
    }

    private void filterAndDisplayPhotos() {
        List<Photo> filtered = new ArrayList<>();
        for (Photo p : allPhotos) {
            boolean matchesTab = !isFavoritesOnly || p.isFavorite();
            boolean matchesSearch = searchQuery.isEmpty() ||
                    p.getTitle().toLowerCase().contains(searchQuery.toLowerCase());

            if (matchesTab && matchesSearch) {
                filtered.add(p);
            }
        }

        photoAdapter.setPhotos(filtered);

        if (filtered.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerViewPhotos.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerViewPhotos.setVisibility(View.VISIBLE);
        }
    }

    private void updateTabButtonStyles() {
        if (isFavoritesOnly) {
            btnFilterFavorites.setBackgroundColor(getColor(R.color.primary_blue));
            btnFilterFavorites.setTextColor(Color.WHITE);

            btnFilterAll.setBackgroundColor(Color.LTGRAY);
            btnFilterAll.setTextColor(Color.BLACK);
        } else {
            btnFilterAll.setBackgroundColor(getColor(R.color.primary_blue));
            btnFilterAll.setTextColor(Color.WHITE);

            btnFilterFavorites.setBackgroundColor(Color.LTGRAY);
            btnFilterFavorites.setTextColor(Color.BLACK);
        }
    }
}
