package com.example.a71;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a71.Advert;
import com.example.a71.AdvertContract;
import com.example.a71.DbHelper;
import com.example.a71.R;

public class AdvertDetailsActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private TextView textViewTitle;
    private TextView textViewDate;
    private TextView textViewLocation;
    private Button btnDeleteAdvert;
    private int advertId; // Added variable to store advert ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_details);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDate = findViewById(R.id.textViewDate);
        textViewLocation = findViewById(R.id.textViewLocation);
        btnDeleteAdvert = findViewById(R.id.btnDeleteAdvert);

        // Retrieve the advert object and advert ID from the intent
        Advert advert = getIntent().getParcelableExtra("advert");
        String advertId = getIntent().getStringExtra("advertId");

        // Print out the advert object and its ID
        Log.d("AdvertDetailsActivity", "Advert: " + advert.toString());
        Log.d("AdvertDetailsActivity", "Advert ID: " + advertId);

        // Set the title, date, and location in the text views
        String title = advert.getPostType() + ": " + advert.getDescription();
        textViewTitle.setText(title);
        textViewDate.setText(advert.getDate());
        textViewLocation.setText(advert.getLocation());
        btnDeleteAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the deletion of the advert
                deleteAdvert(advertId);

                // Finish the activity and return to the previous screen
                finish();
            }
        });

        // Get the database instance from DbHelper
        DbHelper dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
    }


    private void deleteAdvert(String advertId) {
        String deletedAdvertId = String.valueOf(advertId);
        if (advertId != null) {
            // Convert advertId to an integer
            int id = Integer.parseInt(advertId);

            // Define the table name and the where clause for deletion
            String tableName = AdvertContract.AdvertEntry.TABLE_NAME;
            String whereClause = AdvertContract.AdvertEntry._ID + " = ?";
            String[] whereArgs = {String.valueOf(id)};

            // Execute the delete operation
            int rowsDeleted = database.delete(tableName, whereClause, whereArgs);

            if (rowsDeleted > 0) {
                // Deletion successful
                // You can show a success message or perform any other necessary operations
                Log.d("AdvertDetailsActivity", "Advert deleted successfully.");
            } else {
                // Deletion failed
                // You can show an error message or perform any other necessary operations
                Log.d("AdvertDetailsActivity", "Failed to delete advert.");
            }
        } else {
            // Unable to retrieve advert ID
            // You can show an error message or perform any other necessary operations
            Log.d("AdvertDetailsActivity", "Unable to retrieve advert ID.");
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("deletedAdvertId", deletedAdvertId); // Use the variable
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the database connection
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
