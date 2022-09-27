package com.example.pcdv0;

import static com.example.pcdv0.LoginActivity.typeUser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcdv0.databinding.ActivityUserDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailActivity extends AppCompatActivity {

    ActivityUserDetailBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        String id, name = "0", fname, email, tel, state, type;


        if(intent != null) {
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            fname = intent.getStringExtra("fname");
            email = intent.getStringExtra("email");
            tel = intent.getStringExtra("tel");
            state = intent.getStringExtra("state");
            type = intent.getStringExtra("type");


            binding.name.setText(name);
            binding.fname.setText(fname);
            binding.email.setText(email);
            binding.tel.setText(tel);
            binding.type.setText(type);
            binding.state.setText(state);

            System.out.println("type : " + type);


            binding.ivFleche.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            //no button "set as admin" for admins
            if (type.equals("admin")){
                binding.setBtn.setVisibility(View.INVISIBLE);
            }


            System.out.println("id: "+id);

            firebaseDatabase = FirebaseDatabase.getInstance();
            reference = firebaseDatabase.getReference("users").child(id).child("type");

            binding.setBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("are you sure ?");


                    //button cancel
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    dialogInterface.dismiss();
                                }
                            });

                    //button confirm
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    //backend
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            reference.setValue("admin");
                                            Intent intent = new Intent(UserDetailActivity.this, UserNavigationDrawerActivity.class);
                                            intent.putExtra("typeUser", typeUser); //assert that the user is admin
                                            Toast.makeText(UserDetailActivity.this, "setted as admin", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(UserDetailActivity.this, "failed to upgrade user", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                    alertDialog.show();

                }
            });

        }

    }
}