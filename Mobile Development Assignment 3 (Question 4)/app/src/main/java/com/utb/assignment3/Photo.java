package com.utb.assignment3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Photo implements Serializable {
    private String id;
    private String title;
    private String description;
    private String imageUri;
    private boolean isFavorite;
    private long timestamp;

    public Photo(String id, String title, String description, String imageUri, boolean isFavorite, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.isFavorite = isFavorite;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("title", title);
        obj.put("description", description);
        obj.put("imageUri", imageUri);
        obj.put("isFavorite", isFavorite);
        obj.put("timestamp", timestamp);
        return obj;
    }

    public static Photo fromJSONObject(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String title = obj.getString("title");
        String description = obj.optString("description", "");
        String imageUri = obj.getString("imageUri");
        boolean isFavorite = obj.optBoolean("isFavorite", false);
        long timestamp = obj.optLong("timestamp", System.currentTimeMillis());
        return new Photo(id, title, description, imageUri, isFavorite, timestamp);
    }
}
