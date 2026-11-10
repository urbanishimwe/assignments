package com.utb.assignment3;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PhotoUnitTest {

    @Test
    public void photoSerialization_isCorrect() throws JSONException {
        Photo photo = new Photo(
                "test-id-123",
                "Sunset Beach",
                "A beautiful golden sunset over calm ocean waves.",
                "file:///storage/emulated/0/Pictures/sunset.jpg",
                true,
                1600000000000L
        );

        JSONObject json = photo.toJSONObject();
        assertNotNull(json);
        assertEquals("test-id-123", json.getString("id"));
        assertEquals("Sunset Beach", json.getString("title"));
        assertEquals("A beautiful golden sunset over calm ocean waves.", json.getString("description"));
        assertEquals("file:///storage/emulated/0/Pictures/sunset.jpg", json.getString("imageUri"));
        assertTrue(json.getBoolean("isFavorite"));
        assertEquals(1600000000000L, json.getLong("timestamp"));

        Photo restoredPhoto = Photo.fromJSONObject(json);
        assertNotNull(restoredPhoto);
        assertEquals(photo.getId(), restoredPhoto.getId());
        assertEquals(photo.getTitle(), restoredPhoto.getTitle());
        assertEquals(photo.getDescription(), restoredPhoto.getDescription());
        assertEquals(photo.getImageUri(), restoredPhoto.getImageUri());
        assertEquals(photo.isFavorite(), restoredPhoto.isFavorite());
        assertEquals(photo.getTimestamp(), restoredPhoto.getTimestamp());
    }
}
