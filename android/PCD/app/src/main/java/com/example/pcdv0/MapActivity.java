package com.example.pcdv0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.bluettooth.BluetoothActivity;
import com.example.pcdv0.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private ActivityMapBinding binding;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Intent intent = this.getIntent();
        if (intent != null){
            String locate = intent.getStringExtra("message");
            if (locate != null){

                String[] latlong = locate.split(",", 2);
                Double lat = (Double) Double.parseDouble(latlong[0]);
                Double lon = (Double) Double.parseDouble(latlong[1]);
                LatLng loc = new LatLng(lat,lon);

                //async map
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync((new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        // when map is loaded

                        //set area
                        LatLng ownLocation= new LatLng(36.81392137117466, 10.063779491845622);
                        float zoom=12;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownLocation, zoom));
                        location = new Location();



                        MarkerOptions markerOptions = new MarkerOptions();
                        //set position of marker
                        markerOptions.position(loc);
                        //set title of marker
                        markerOptions.title(loc.latitude+ " : " + loc.longitude);
                        //zoom
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18)
                        );
                        //add marker on map
                        googleMap.addMarker(markerOptions);

                    }
                }
                ));
            }

            else{
                //async map
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync((new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        // when map is loaded

                        //set area
                        LatLng ownLocation= new LatLng(36.81392137117466, 10.063779491845622);
                        float zoom=12;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownLocation, zoom));
                        location = new Location();

                        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                            @Override
                            public void onMapLongClick(@NonNull LatLng latLng) {
                                //when long clicked on map
                                //initialize marker
                                MarkerOptions markerOptions = new MarkerOptions();
                                //set position of marker
                                markerOptions.position(latLng);
                                //set title of marker
                                markerOptions.title(latLng.latitude+ " : " + latLng.longitude);
                                //remove all marker
                                googleMap.clear();
                                //zoom
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18)
                                );
                                //add marker on map
                                googleMap.addMarker(markerOptions);


                                location.setLatitude(latLng.latitude);
                                location.setLongitude(latLng.longitude);
                            }
                        });
                    }
                }
                ));
            }
        }



        binding.getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, BluetoothActivity.class);
                String location = "location";
                intent.putExtra("location", location);
                startActivity(intent);
            }
        });

        binding.confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, AddCarActivity.class);
                intent.putExtra("latitude",location.getLatitude());
                intent.putExtra("longitude",location.getLongitude());

                startActivity(intent);
            }
        });

    }
}