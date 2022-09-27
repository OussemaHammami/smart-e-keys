package com.example.pcdv0;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    public static String typeUser = "0000";

    TextInputEditText etEmail, etPassword;
    Button btnSignIn;
    TextView tvCreateAccount;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private String nameFromDB="name" ;

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public static void setTypeUser(String ty){
        typeUser = ty;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPwd);
        btnSignIn= findViewById(R.id.btnSignin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        mAuth = FirebaseAuth.getInstance();
        loadingPB = findViewById(R.id.idPBLoading);


        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCreateCompteActivity();

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiding our progress bar.
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text on below line.
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.username_password_required), Toast.LENGTH_SHORT).show();
                }

                else {

                    if (email.equals("admin")  && password.equals("admin")){

                        Toast.makeText(LoginActivity.this, "welcome admin", Toast.LENGTH_SHORT).show();
                        startActivityUserMain();
                        typeUser = "admin";

                    }

                    else if(email.equals("client")  && password.equals("client")){
                        Toast.makeText(LoginActivity.this, "welcome client", Toast.LENGTH_SHORT).show();
                        startActivityUserMain();
                        typeUser = "client";
                    }

                    else{
                        // on below line we are calling a sign in method and passing email and password to it.
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //check if the task is success or not.

                                if (task.isSuccessful()) {
                                    //hide progress bar.
                                    loadingPB.setVisibility(View.GONE);
                                    String userId = mAuth.getCurrentUser().getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                                    Query query = ref.orderByKey().equalTo(userId);
                                    System.out.println("query: "+query.getRef());
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                typeUser = snapshot.child(userId).child("type").getValue(String.class);
                                                nameFromDB = snapshot.child(userId).child("name").getValue(String.class);
                                                System.out.println("name: "+nameFromDB);
                                                Toast.makeText(LoginActivity.this, "Welcome "+nameFromDB, Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(LoginActivity.this, "data cancelled ", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                    startActivityUserMain();
                                    finish();
                                } else {
                                    loadingPB.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Please enter valid user credentials..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    System.out.println("typeUser "+ typeUser);

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //checking if the user is already sign in.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // if the user is not null then we are
            // opening a main activity on below line.
            startActivityUserMain();
            this.finish();
        }
    }

    private void startCreateCompteActivity() {

        Intent intent2 = new Intent(LoginActivity.this, CreateAccountActivity.class);

        startActivity(intent2);

    }

    private void startActivityUserMain() {
        Intent intent = new Intent(LoginActivity.this, UserNavigationDrawerActivity.class);

        startActivityForResult(intent, 1);

    }
}



