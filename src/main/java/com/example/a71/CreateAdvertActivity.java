package com.example.a71;

import static com.example.a71.SearchActivity.REQUEST_CODE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_LOCATION = 100;
    private static final int REQUEST_CODE_SEARCH = 101;

    private EditText etName;
    private EditText etPhoneNumber;
    private EditText etDescription;
    private EditText etDate;
    private EditText etLocation;
    private Button btnGetCurrentLocation;
    private Button btnSubmit;

    private EditText etLatitude;
    private EditText etLongitude;
    private String addressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialize views
        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);


        // Set click listener for Get Current Location button
        btnGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check location permission
                if (ContextCompat.checkSelfPermission(CreateAdvertActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    // Request location permission
                    ActivityCompat.requestPermissions(CreateAdvertActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
            }
        });

        // Set click listener for Location input box
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch SearchActivity when Location input box is clicked
                Intent intent = new Intent(CreateAdvertActivity.this, SearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEARCH);
            }
        });

        // Set click listener for Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered advert details
                String name = etName.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                String location = etLocation.getText().toString().trim();
                double latitude = Double.parseDouble(etLatitude.getText().toString().trim());
                double longitude = Double.parseDouble(etLongitude.getText().toString().trim());

                // Validate the input fields (add your validation logic here)

                // Create a new instance of DbHelper
                DbHelper dbHelper = new DbHelper(CreateAdvertActivity.this);

                // Add the advert item to the database
                long insertedRowId = dbHelper.insertAdvert(name, phoneNumber, description, date, location, latitude, longitude);

                if (insertedRowId != -1) {
                    // Show a toast message to indicate successful submission
                    Toast.makeText(CreateAdvertActivity.this, "Advert submitted successfully", Toast.LENGTH_SHORT).show();

                    // Go back to the MainActivity
                    Intent intent = new Intent(CreateAdvertActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Show a toast message to indicate failure
                    Toast.makeText(CreateAdvertActivity.this, "Failed to submit advert", Toast.LENGTH_SHORT).show();
                }

                // Close the database connection
                dbHelper.close();
            }
        });
    }

    private void getCurrentLocation() {
        // Check if the required location permissions are granted
        if (ActivityCompat.checkSelfPermission(CreateAdvertActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(CreateAdvertActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the missing permissions if not granted
            ActivityCompat.requestPermissions(CreateAdvertActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
            return;
        }

        // Create a FusedLocationProviderClient
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the last known location
        fusedLocationClient.getLastLocation().addOnSuccessListener(CreateAdvertActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    geocodeLocation(latitude, longitude);
                    etLatitude.setText(String.valueOf(latitude));
                    etLongitude.setText(String.valueOf(longitude));
                } else {
                    Toast.makeText(CreateAdvertActivity.this, "Failed to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void geocodeLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(CreateAdvertActivity.this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();

                // Append address lines to StringBuilder
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i));
                    if (i < address.getMaxAddressLineIndex()) {
                        sb.append(", ");
                    }
                }

                // Update the addressText variable and set it to the EditText
                addressText = sb.toString();
                etLocation.setText(addressText);

                // Set latitude and longitude values to the respective EditText fields
                String latitudeString = String.valueOf(latitude);
                String longitudeString = String.valueOf(longitude);
                etLatitude.setText(latitudeString);
                etLongitude.setText(longitudeString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update the onActivityResult method in CreateAdvertActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH && resultCode == RESULT_OK && data != null) {
            String selectedPlaceName = data.getStringExtra("selectedPlaceName");
            String selectedPlaceId = data.getStringExtra("selectedPlaceId");
            String selectedPlaceAddress = data.getStringExtra("selectedPlaceAddress");

            // Create a new instance of PlacesClient
            PlacesClient placesClient = Places.createClient(this);

            // Fetch place details using the place ID
            List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);
            FetchPlaceRequest request = FetchPlaceRequest.builder(selectedPlaceId, placeFields).build();
            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    double selectedPlaceLatitude = latLng.latitude;
                    double selectedPlaceLongitude = latLng.longitude;

                    // Set the selected place data in the input boxes
                    etLocation.setText(selectedPlaceAddress);
                    etLatitude.setText(String.valueOf(selectedPlaceLatitude));
                    etLongitude.setText(String.valueOf(selectedPlaceLongitude));
                }
            }).addOnFailureListener((exception) -> {
                // Handle failure to fetch place details
                Toast.makeText(this, "Failed to fetch place details", Toast.LENGTH_SHORT).show();
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                getCurrentLocation();
            } else {
                // Location permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
