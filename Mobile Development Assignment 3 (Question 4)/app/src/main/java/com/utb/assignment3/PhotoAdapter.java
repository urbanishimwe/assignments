package com.utb.assignment3;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private static final String TAG = "PhotoAdapter";

    public interface OnPhotoClickListener {
        void onPhotoClick(Photo photo);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Photo photo);
    }

    private final Context context;
    private List<Photo> photos;
    private final OnPhotoClickListener clickListener;
    private final OnFavoriteClickListener favoriteListener;

    public PhotoAdapter(Context context, List<Photo> photos,
                        OnPhotoClickListener clickListener,
                        OnFavoriteClickListener favoriteListener) {
        this.context = context;
        this.photos = photos;
        this.clickListener = clickListener;
        this.favoriteListener = favoriteListener;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        holder.txtTitle.setText(photo.getTitle());

        if (photo.isFavorite()) {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        loadPhotoUri(holder.imgThumbnail, photo.getImageUri());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPhotoClick(photo);
            }
        });

        holder.btnFavorite.setOnClickListener(v -> {
            if (favoriteListener != null) {
                favoriteListener.onFavoriteClick(photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos != null ? photos.size() : 0;
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

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtTitle;
        ImageButton btnFavorite;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}
