package com.example.a71;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateAdvert;
    private Button btnShowAllItems;
    private Button btnShowOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowAllItems = findViewById(R.id.btnShowAllItems);
        btnShowOnMap = findViewById(R.id.btnShowOnMap);

        // Set click listeners
        btnCreateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open CreateAdvertActivity
                Intent intent = new Intent(MainActivity.this, CreateAdvertActivity.class);
                startActivity(intent);
            }
        });

        btnShowAllItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ShowAllItemActivity
                Intent intent = new Intent(MainActivity.this, ShowAllItemsActivity.class);
                startActivity(intent);
            }
        });


        btnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MapActivity
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}
