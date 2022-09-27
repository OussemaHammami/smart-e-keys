package com.example.pcdv0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.databinding.ActivityCarDetailBinding;
import com.example.pcdv0.ui.cars.Car;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarDetailActivity extends AppCompatActivity {

    ActivityCarDetailBinding binding;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference  geodatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        String number = "0", name, traveled, place, price, state;
        Double longitude, latitude;


        if(intent != null){
            name = intent.getStringExtra("name");
            number = intent.getStringExtra("serial_number");
            traveled = intent.getStringExtra("traveled");
            place = intent.getStringExtra("place");
            price = intent.getStringExtra("price");
            state = intent.getStringExtra("state");


            binding.name.setText(name);
            binding.serialNumber.setText(number);
            binding.traveled.setText(traveled);
            binding.place.setText(place);
            binding.price.setText(price);
            binding.state.setText(state);
            binding.ivPorche.setImageResource(Car.getImageCar(name));

            System.out.println("number: "+number);

        }

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.ivFleche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String finalNumber = number;

        //rent and go to rent activity
        binding.btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarDetailActivity.this,RentActivity.class);
                intent.putExtra("serial_number", finalNumber);

                startActivity(intent);
            }
        });


        //getLocations
        firebaseDatabase = FirebaseDatabase.getInstance();

        geodatabaseReference = firebaseDatabase.getReference("locations");
        GeoFire geoFire = new GeoFire(geodatabaseReference);



        //show car localisation on map
        binding.showOnMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarDetailActivity.this, ShowMapActivity.class);

                //get location from db
                geoFire.getLocation(finalNumber, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location != null){
                            System.out.println("latitude: "+ location.latitude+ "longitude: "+ location.longitude);
                            Location locate = new Location(location.longitude, location.latitude);
                            intent.putExtra("longitude" , location.longitude);
                            intent.putExtra("latitude" , location.latitude);
                            startActivity(intent);
                        }

                        else
                        {
                            System.out.println("There is no location for key "+ key);
                            Toast.makeText(CarDetailActivity.this, "error from db", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });

    }
}