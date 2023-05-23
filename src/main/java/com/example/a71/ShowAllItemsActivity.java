package com.example.a71;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a71.Advert;
import com.example.a71.AdvertContract;
import com.example.a71.AdvertDetailsActivity;
import com.example.a71.AdvertListAdapter;
import com.example.a71.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class ShowAllItemsActivity extends AppCompatActivity {

    private ListView listViewAdverts;
    private List<Advert> advertList;
    private AdvertListAdapter adapter;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_items);

        dbHelper = new DbHelper(this);

        listViewAdverts = findViewById(R.id.listViewAdverts);

        // Retrieve the list of adverts from the SQLite database and set the adapter
        retrieveAdvertsFromDatabase();

        // Set a click listener for the items in the ListView
        listViewAdverts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Advert advert = advertList.get(position);
                showAdvertDetails(advert);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String deletedAdvertId = data.getStringExtra("deletedAdvertId");
            if (deletedAdvertId != null) {
                // Remove the deleted item from the advertList
                for (Advert advert : advertList) {
                    if (String.valueOf(advert.getId()).equals(deletedAdvertId)) {
                        advertList.remove(advert);
                        break;
                    }
                }

                // Notify the adapter that the dataset has changed
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the list whenever the activity is resumed
        retrieveAdvertsFromDatabase();
    }

    private void retrieveAdvertsFromDatabase() {
        advertList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                AdvertContract.AdvertEntry._ID,
                AdvertContract.AdvertEntry.COLUMN_POST_TYPE,
                AdvertContract.AdvertEntry.COLUMN_DESCRIPTION,
                AdvertContract.AdvertEntry.COLUMN_DATE,
                AdvertContract.AdvertEntry.COLUMN_LOCATION,
                AdvertContract.AdvertEntry.COLUMN_LATITUDE,
                AdvertContract.AdvertEntry.COLUMN_LONGITUDE
        };

        Cursor cursor = db.query(
                AdvertContract.AdvertEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry._ID);
            int postTypeIndex = cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_POST_TYPE);
            int descriptionIndex = cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_DESCRIPTION);
            int dateIndex = cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_DATE);
            int locationIndex = cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_LOCATION);
            int latitudeIndex = cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_LATITUDE);
            int longitudeIndex = cursor.getColumnIndexOrThrow(AdvertContract.AdvertEntry.COLUMN_LONGITUDE);

            int id = cursor.getInt(idIndex);
            String postType = cursor.getString(postTypeIndex);
            String description = cursor.getString(descriptionIndex);
            String date = cursor.getString(dateIndex);
            String location = cursor.getString(locationIndex);
            double latitude = cursor.getDouble(latitudeIndex);
            double longitude = cursor.getDouble(longitudeIndex);

            Advert advert = new Advert(id, postType, description, date, location, latitude, longitude);
            advertList.add(advert);
        }

        cursor.close();
        db.close();

        // Create and set the adapter for the ListView
        adapter = new AdvertListAdapter(this, advertList);
        listViewAdverts.setAdapter(adapter);
    }


    private void showAdvertDetails(Advert advert) {
        Intent intent = new Intent(ShowAllItemsActivity.this, AdvertDetailsActivity.class);
        intent.putExtra("advert", advert);
        intent.putExtra("advertId", String.valueOf(advert.getId()));
        startActivity(intent);
    }
}
