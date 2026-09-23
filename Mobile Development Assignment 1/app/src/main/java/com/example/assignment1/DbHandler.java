package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class DbHandler extends SQLiteOpenHelper {
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_LOC = "location";
    static final String KEY_DESG = "designation";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usersdb";
    private static final String TABLE_Users = "userdetails";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static Boolean valuesValid(TextView name, TextView location, TextView designation) {
        if (name.getText() == null || location.getText() == null || designation.getText() == null)
            return false;
        String nameText = name.getText().toString();
        String locationText = location.getText().toString();
        String designationText = designation.getText().toString();
        if (nameText.trim().isEmpty() || locationText.trim().isEmpty() || designationText.trim().isEmpty())
            return false;
        return true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Users + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_LOC + " TEXT," + KEY_DESG + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Users); // Create tables again
        onCreate(db);
    }

    public long insert(String name, String loc, String desg) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_LOC, loc);
        values.put(KEY_DESG, desg);

        return db.insert(TABLE_Users, null, values);
    }

    public ArrayList<Map<String, String>> getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {KEY_ID, KEY_NAME, KEY_LOC, KEY_DESG};

        ArrayList<Map<String, String>> users = new ArrayList<>();
        try (Cursor cursor = db.query(
                // The table to query
                TABLE_Users,
                // The array of columns to return (pass null to get all)
                projection,
                // The columns for the WHERE clause
                null,
                // The values for the WHERE clause
                null,
                // don't group the rows
                null,
                // don't filter by row groups
                null,
                // The sort order
                KEY_ID + " ASC")) {

            while (cursor.moveToNext()) {
                Map<String, String> user = Map.of(
                        // id: value
                        KEY_ID, String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID))),
                        // name: value
                        KEY_NAME, cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        // location: value
                        KEY_LOC, cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOC)),
                        // designation: value
                        KEY_DESG, cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESG)));
                users.add(user);
            }
        }
        return users;
    }

    public void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_Users, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void update(long id, String name, String loc, String desg) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_LOC, loc);
        values.put(KEY_DESG, desg);
        db.update(TABLE_Users, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
