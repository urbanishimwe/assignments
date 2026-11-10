package com.utb.assignment3;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoStorageManager {
    private static final String PREF_NAME = "PhotoAppPrefs";
    private static final String KEY_PHOTOS = "photo_list_json";
    private static final String TAG = "PhotoStorageManager";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static List<Photo> getPhotos(Context context) {
        SharedPreferences prefs = getPrefs(context);
        String jsonString = prefs.getString(KEY_PHOTOS, null);

        if (jsonString == null || jsonString.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Photo> photos = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                photos.add(Photo.fromJSONObject(obj));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing saved photos JSON", e);
        }

        return photos;
    }

    public static void savePhotos(Context context, List<Photo> photos) {
        JSONArray array = new JSONArray();
        for (Photo photo : photos) {
            try {
                array.put(photo.toJSONObject());
            } catch (JSONException e) {
                Log.e(TAG, "Error converting photo to JSON", e);
            }
        }
        getPrefs(context).edit().putString(KEY_PHOTOS, array.toString()).apply();
    }

    public static void addPhoto(Context context, Photo photo) {
        List<Photo> photos = getPhotos(context);
        photos.add(0, photo);
        savePhotos(context, photos);
    }

    public static void updatePhoto(Context context, Photo updatedPhoto) {
        List<Photo> photos = getPhotos(context);
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).getId().equals(updatedPhoto.getId())) {
                photos.set(i, updatedPhoto);
                break;
            }
        }
        savePhotos(context, photos);
    }

    public static void deletePhoto(Context context, String photoId) {
        List<Photo> photos = getPhotos(context);
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).getId().equals(photoId)) {
                photos.remove(i);
                break;
            }
        }
        savePhotos(context, photos);
    }

    public static String saveImageToInternalStorage(Context context, Uri sourceUri) {
        try {
            File photosDir = new File(context.getFilesDir(), "user_photos");
            if (!photosDir.exists()) {
                if (!photosDir.mkdirs()) {
                    Log.w(TAG, "Failed to create directory or directory exists");
                }
            }

            File destFile = new File(photosDir, "photo_" + System.currentTimeMillis() + ".jpg");
            ContentResolver resolver = context.getContentResolver();

            try (InputStream in = resolver.openInputStream(sourceUri);
                 OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[4096];
                int read;
                if (in != null) {
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                }
            }

            return Uri.fromFile(destFile).toString();
        } catch (Exception e) {
            Log.e(TAG, "Failed to copy image to internal storage", e);
            return sourceUri.toString();
        }
    }
}
