package com.example.a71;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize the DbHelper object
        dbHelper = new DbHelper(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get all adverts from the database
        List<Advert> advertList = dbHelper.getAllAdverts();

        // Add markers for each advert item
        for (Advert advert : advertList) {
            LatLng location = new LatLng(advert.getLatitude(), advert.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(advert.getDescription()));
        }

        // Move the camera to the last advert's location if available
        if (!advertList.isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Advert advert : advertList) {
                LatLng location = new LatLng(advert.getLatitude(), advert.getLongitude());
                builder.include(location);
            }
            LatLngBounds bounds = builder.build();
            int padding = 100; // Adjust this padding as per your requirement
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cameraUpdate);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed
        dbHelper.close();
    }
}
