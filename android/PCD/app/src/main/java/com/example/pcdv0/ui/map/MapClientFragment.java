package com.example.pcdv0.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pcdv0.CarDetailActivity;
import com.example.pcdv0.R;
import com.example.pcdv0.databinding.FragmentMapClientBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MapClientFragment extends Fragment {




    @Override
        public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState){

        FragmentMapClientBinding binding;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;


        //initialaize view
            binding = FragmentMapClientBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            //initialize map fragment
            SupportMapFragment supportMapFragment = (SupportMapFragment)
                    getChildFragmentManager().findFragmentById(R.id.google_map_client);



            //get locations from db
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference= firebaseDatabase.getReference("locations");
            GeoFire geoFire = new GeoFire(databaseReference);
            // creates a new query around tunis
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(36.81392137117466, 10.063779491845622), 400);



            //async map
            assert supportMapFragment != null;
            supportMapFragment.getMapAsync((new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    // when map is loaded

                    //set area
                    LatLng ownLocation = new LatLng(36.81392137117466, 10.063779491845622);
                    float zoom = 10;
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ownLocation, zoom));



                    //get markers
                    geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                        @Override
                        public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(location.latitude,location.longitude));
                            markerOptions.title(dataSnapshot.getKey());
                            //add marker on map
                            googleMap.addMarker(markerOptions);
                        }

                        @Override
                        public void onDataExited(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

                        }

                        @Override
                        public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });

                    //query all cars from db
                    geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                        @Override
                        public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {

                            //filter available cars
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cars").child(Objects.requireNonNull(dataSnapshot.getKey()));
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String state = snapshot.child("state").getValue(String.class);
                                    if (state.equals("available")){
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(new LatLng(location.latitude,location.longitude));
                                        markerOptions.title(dataSnapshot.getKey());
                                        //add marker on map
                                        googleMap.addMarker(markerOptions);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }

                        @Override
                        public void onDataExited(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {


                        }

                        @Override
                        public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {
                            System.err.println("There was an error with this query: " + error);
                        }
                    });


                    //go to car detail activity when clicking on marker
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            String key =marker.getTitle();
                            assert key != null;

                            //get details from db by id
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cars").child(key);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Intent intent = new Intent(getContext(), CarDetailActivity.class);

                                    String name = snapshot.child("name").getValue(String.class);
                                    String serialNumber = snapshot.child("serialNumber").getValue(String.class);
                                    String traveled = snapshot.child("traveled").getValue(String.class);
                                    String place = snapshot.child("place").getValue(String.class);
                                    String price = snapshot.child("price").getValue(String.class);

                                    intent.putExtra("name", name);
                                    intent.putExtra("serial_number",serialNumber);
                                    intent.putExtra("traveled", traveled);
                                    intent.putExtra("place", place);
                                    intent.putExtra("price", price);

                                    //go to car detail activity with details
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), "db error", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return false;
                        }
                    });
                }
            }
            ));

            return root;
        }


}