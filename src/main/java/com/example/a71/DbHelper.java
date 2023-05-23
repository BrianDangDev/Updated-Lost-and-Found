package com.example.a71;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "adverts.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ADVERTS_TABLE =
                "CREATE TABLE " + AdvertContract.AdvertEntry.TABLE_NAME + " (" +
                        AdvertContract.AdvertEntry._ID + " INTEGER PRIMARY KEY," +
                        AdvertContract.AdvertEntry.COLUMN_POST_TYPE + " TEXT," +
                        AdvertContract.AdvertEntry.COLUMN_NAME + " TEXT," +
                        AdvertContract.AdvertEntry.COLUMN_PHONE_NUMBER + " TEXT," +
                        AdvertContract.AdvertEntry.COLUMN_DESCRIPTION + " TEXT," +
                        AdvertContract.AdvertEntry.COLUMN_DATE + " TEXT," +
                        AdvertContract.AdvertEntry.COLUMN_LOCATION + " TEXT," +
                        AdvertContract.AdvertEntry.COLUMN_LATITUDE + " REAL," +
                        AdvertContract.AdvertEntry.COLUMN_LONGITUDE + " REAL)";

        db.execSQL(SQL_CREATE_ADVERTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }

    public long insertAdvert(String name, String phoneNumber, String description, String date, String location,
                             double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AdvertContract.AdvertEntry.COLUMN_NAME, name);
        values.put(AdvertContract.AdvertEntry.COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(AdvertContract.AdvertEntry.COLUMN_DESCRIPTION, description);
        values.put(AdvertContract.AdvertEntry.COLUMN_DATE, date);
        values.put(AdvertContract.AdvertEntry.COLUMN_LOCATION, location);
        values.put(AdvertContract.AdvertEntry.COLUMN_LATITUDE, latitude);
        values.put(AdvertContract.AdvertEntry.COLUMN_LONGITUDE, longitude);

        // You can set default values for other columns if needed

        return db.insert(AdvertContract.AdvertEntry.TABLE_NAME, null, values);
    }

    public List<Advert> getAllAdverts() {
        List<Advert> advertList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                AdvertContract.AdvertEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry._ID));
                String postType = cursor.getString(cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_POST_TYPE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_DATE));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_LOCATION));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_LONGITUDE));

                Advert advert = new Advert(id, postType, description, date, location, latitude, longitude);
                advert.setLatitude(latitude);
                advert.setLongitude(longitude);
                advertList.add(advert);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return advertList;
    }
}
