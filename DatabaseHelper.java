package com.example.sosyalsorumluluk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sosyal_sorumluluk.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";

    // Animals table
    private static final String TABLE_ANIMALS = "animals";
    private static final String COL_ANIMAL_ID = "id";
    private static final String COL_ANIMAL_TYPE = "type";
    private static final String COL_ANIMAL_LOCATION = "location";
    private static final String COL_ANIMAL_VISIT_COUNT = "visit_count";
    private static final String COL_ANIMAL_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_USER_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createUsersTable);

        // Create animals table
        String createAnimalsTable = "CREATE TABLE " + TABLE_ANIMALS + " (" +
                COL_ANIMAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ANIMAL_TYPE + " TEXT NOT NULL, " +
                COL_ANIMAL_LOCATION + " TEXT NOT NULL, " +
                COL_ANIMAL_VISIT_COUNT + " INTEGER DEFAULT 0, " +
                COL_ANIMAL_IMAGE + " BLOB)";
        db.execSQL(createAnimalsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS);
        onCreate(db);
    }

    // User operations
    public long addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_USER_ID};
        String selection = COL_USER_EMAIL + " = ? AND " + COL_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public boolean userExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_USER_ID};
        String selection = COL_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // Animal operations
    public long addAnimal(String type, String location, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ANIMAL_TYPE, type);
        values.put(COL_ANIMAL_LOCATION, location);
        values.put(COL_ANIMAL_VISIT_COUNT, 0);
        values.put(COL_ANIMAL_IMAGE, imageBytes);
        long result = db.insert(TABLE_ANIMALS, null, values);
        db.close();
        return result;
    }

    public ArrayList<Animal> getAllAnimals() {
        ArrayList<Animal> animalsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANIMALS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ANIMAL_ID));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANIMAL_TYPE));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANIMAL_LOCATION));
                long visitCount = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ANIMAL_VISIT_COUNT));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_ANIMAL_IMAGE));

                // Convert byte array to image path (we'll store as base64 or use file path)
                String imagePath = null;
                if (imageBytes != null) {
                    // For simplicity, we'll use a placeholder approach
                    // In a real app, you might save images to internal storage
                    imagePath = "local_image_" + id;
                }

                animalsList.add(new Animal(String.valueOf(id), type, location, visitCount, imagePath, imageBytes));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return animalsList;
    }

    public boolean updateVisitCount(String animalId, long newVisitCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ANIMAL_VISIT_COUNT, newVisitCount);
        int result = db.update(TABLE_ANIMALS, values, COL_ANIMAL_ID + " = ?", new String[]{animalId});
        db.close();
        return result > 0;
    }
}

