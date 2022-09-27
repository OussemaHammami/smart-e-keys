package com.example.pcdv0;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.databinding.ActivityShowMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShowMapBinding binding = ActivityShowMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Intent intent = this.getIntent();
        if (intent != null) {
            Double longitude = intent.getDoubleExtra("longitude", 0.00);  //location from previous activity
            Double latitude = intent.getDoubleExtra("latitude", 0.00);


            //async map
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync((new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    // when map is loaded

                    //set area
                    LatLng ownLocation = new LatLng(36.81392137117466, 10.063779491845622);
                    float zoom = 12;
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownLocation, zoom));
                    LatLng latLng = new LatLng(latitude, longitude);
                    //initialize marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    //set position of marker
                    markerOptions.position(latLng);
                    //set title of marker
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    //zoom
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)
                    );
                    //add marker on map
                    googleMap.addMarker(markerOptions);

                }
            }
            ));
        }
    }
}