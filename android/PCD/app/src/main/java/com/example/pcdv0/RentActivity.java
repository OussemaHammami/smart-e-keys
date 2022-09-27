package com.example.pcdv0;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.databinding.ActivityRentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class RentActivity extends AppCompatActivity {

    ActivityRentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = this.getIntent();


        String carId= intent.getStringExtra("serial_number");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("rents");

        binding.btnRentCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numAccount= binding.etNumAccount.getText().toString();
                String pwd= binding.etPwd.getText().toString();
                String duration= binding.etDuration.getText().toString();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String dateOfRent = formatter.format(date);

                String userId = user.getUid();

                Rent rent =new Rent(carId,userId,numAccount,pwd,dateOfRent,duration);

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // on below line we are setting data in our firebase database.
                        ref.child(carId+userId).setValue(rent);
                        // displaying a toast message.
                        Toast.makeText(RentActivity.this, "car rented", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RentActivity.this, "rent failed", Toast.LENGTH_SHORT).show();
                    }
                });

                DatabaseReference reference = firebaseDatabase.getReference("cars").child(carId).child("state");
                reference.setValue("rented");

                DatabaseReference reference2 = firebaseDatabase.getReference("users").child(userId).child("rent");
                reference2.setValue("renting");

                startActivity(new Intent(RentActivity.this, UserNavigationDrawerActivity.class));
            }
        });



    }

    public void updateState(){

    }
}