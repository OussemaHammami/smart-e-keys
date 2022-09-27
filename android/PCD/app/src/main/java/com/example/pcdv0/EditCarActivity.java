package com.example.pcdv0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.databinding.ActivityEditCarBinding;
import com.example.pcdv0.ui.cars.Car;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditCarActivity extends AppCompatActivity {

    ActivityEditCarBinding binding;
    Car car;
    private String carID;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCarBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //get car class on which we have passed.
        car = getIntent().getParcelableExtra("car");
        if (car != null) {
            // on below line we are setting data to our edit text from our modal class.
            binding.etCarRegistration.setText(car.getSerialNumber());
            binding.etCarName.setText(car.getName());
            binding.etPlace.setText(car.getPlace());
            binding.etPrice.setText(car.getPrice());
            binding.etTraveledKilometers.setText(car.getTraveled());
            binding.etState.setText((car.getState()));
            carID = car.getSerialNumber();
            System.out.println("carID: "+ carID);
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("cars").child(carID);

        //onClick
        binding.btnEditCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make progress bar as visible.
                binding.idPBLoading.setVisibility(View.VISIBLE);
                // on below line we are getting data from our edit text.
                String serialNumber = binding.etCarRegistration.getText().toString();
                String name = binding.etCarName.getText().toString();
                String place = binding.etPlace.getText().toString();
                String price = binding.etPrice.getText().toString();
                String traveled = binding.etTraveledKilometers.getText().toString();
                String state = binding.etState.getText().toString();

                //create a map for passing a data using key and value pair.
                Map<String, Object> map = new HashMap<>();
                map.put("serialNumber", serialNumber);
                map.put("name", name);
                map.put("place", place);
                map.put("price", price);
                map.put("traveled", traveled);
                map.put("state", state);
                map.put("carId", carID);

                //backend
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // making progress bar visibility as gone.
                        binding.idPBLoading.setVisibility(View.GONE);
                        // adding a map to our database.
                        reference.updateChildren(map);
                        // on below line we are displaying a toast message.
                        Toast.makeText(EditCarActivity.this, "Car Updated..", Toast.LENGTH_SHORT).show();
                        // opening a new activity after updating our coarse.
                        startActivity(new Intent(EditCarActivity.this, UserNavigationDrawerActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditCarActivity.this, "Fail to update car..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}