package com.example.a71;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyDTEvgIhlp4nlNcOJ7byocMh4DwJILDR7U");

        // Create a new PlacesClient instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Field.ID, Field.NAME, Field.ADDRESS));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Get the selected place's name, ID, and address.
                String placeName = place.getName();
                String placeId = place.getId();
                String placeAddress = place.getAddress();

                // Create an intent to pass the selected place data back to the previous activity.
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPlaceName", placeName);
                resultIntent.putExtra("selectedPlaceId", placeId);
                resultIntent.putExtra("selectedPlaceAddress", placeAddress);

                setResult(RESULT_OK, resultIntent);
                finish();
            }




            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
