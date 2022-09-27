package com.example.pcdv0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.ui.cars.Car;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCarActivity extends AppCompatActivity {

    // creating variables for our button, edit text,
    // firebase database, database reference, progress bar.
    private Button addCarBtn, locationBtn;
    private TextInputEditText etCarRegistration, etCarName, etPlace, etTraveled, etPrice;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReferenceGeo;
    private ProgressBar loadingPB;
    private String carID;
    private TextView tvConfirmation;
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        // initializing all our variables.
        addCarBtn = findViewById(R.id.btdAddCar);
        etCarRegistration = findViewById(R.id.etCarRegistration);
        etCarName = findViewById(R.id.etCarName);
        etPlace = findViewById(R.id.etPlace);
        etTraveled = findViewById(R.id.etTraveledKilometers);
        etPrice = findViewById(R.id.etPrice);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //create database reference.
        databaseReference = firebaseDatabase.getReference("cars");
        //location
        databaseReferenceGeo = firebaseDatabase.getReference("locations");



        Intent intent = this.getIntent();

        //getLocation
        longitude = intent.getDoubleExtra("longitude", 0.00);
        latitude = intent.getDoubleExtra("latitude", 0.00);

        // adding click listener for our add course button.
        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String carRegistration = etCarRegistration.getText().toString();
                String carName = etCarName.getText().toString();
                String place = etPlace.getText().toString();
                String traveled = etTraveled.getText().toString();
                String price = etPrice.getText().toString();
                GeoFire geoFire = new GeoFire(databaseReferenceGeo);
                int traveledInt = 0;
                try{
                    traveledInt = Integer.parseInt(traveled);
                    System.out.println(traveledInt);
                }
                catch (NumberFormatException ex){
                    ex.printStackTrace();
                }

                carID = carRegistration;

                //pass data to firebase database.
                System.out.println("longitude: "+ longitude+ "latitude: "+ latitude);
                Car car = new Car(carName, carRegistration, traveled, place, price );
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // on below line we are setting data in our firebase database.
                        databaseReference.child(carID).setValue(car);
                        // displaying a toast message.
                        Toast.makeText(AddCarActivity.this, "Car Added..", Toast.LENGTH_SHORT).show();
                        // starting a main activity.
                        startActivity(new Intent(AddCarActivity.this, UserNavigationDrawerActivity.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on below line.
                        Toast.makeText(AddCarActivity.this, "Fail to add car..", Toast.LENGTH_SHORT).show();
                    }

                });

                //add location
                geoFire.setLocation(carID,new GeoLocation(latitude,longitude));
            }
        });
    }
}

