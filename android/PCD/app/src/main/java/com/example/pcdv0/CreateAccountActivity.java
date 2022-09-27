package com.example.pcdv0;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateAccountActivity extends AppCompatActivity {

    ImageView ivFleche;
    Button btnCancel, btnCreateAccount;
    TextInputEditText etName, etFamilyName, etTel, etPassword, etPassword2, etMail;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressBar loadingPB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etName = (TextInputEditText) findViewById(R.id.etName);
        etFamilyName = (TextInputEditText) findViewById(R.id.etFname);
        etTel = (TextInputEditText) findViewById(R.id.etNumber);
        etMail = (TextInputEditText) findViewById(R.id.etEmail);
        etPassword = (TextInputEditText) findViewById(R.id.etPassword);
        etPassword2 = (TextInputEditText) findViewById(R.id.etPassword2);

        ivFleche = findViewById(R.id.ivFleche);
        btnCancel = findViewById(R.id.btnCancel);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        mAuth = FirebaseAuth.getInstance();
        loadingPB = findViewById(R.id.idPBLoading);


        //cancel (return back to login activity)
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });

        //cancel (return back to login activity)
        ivFleche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });


        //add new account to db
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hiding our progress bar.
                loadingPB.setVisibility(View.VISIBLE);

                // getting data from our edit text.
                String Name = etName.getText().toString();
                String FamilyName = etFamilyName.getText().toString();
                String Email = etMail.getText().toString();
                String Tel = etTel.getText().toString();
                String pwd = etPassword.getText().toString();
                String cnfPwd = etPassword2.getText().toString();

                // checking if the password and confirm password is equal or not.
                if (!pwd.equals(cnfPwd)) {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(CreateAccountActivity.this, "Please check both having same password..", Toast.LENGTH_SHORT).show();
                }

                // checking if the text fields are empty or not.
                else if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(FamilyName) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Tel) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(cnfPwd)) {
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(CreateAccountActivity.this, "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                } else {

                    // create a new user by passing email and password.
                    mAuth.createUserWithEmailAndPassword(Email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            System.out.println(task.isSuccessful());
                            // check if the task is success or not.
                            if (task.isSuccessful()) {
                                //hide our progress bar and opening a login activity.
                                loadingPB.setVisibility(View.GONE);
                                String userID = mAuth.getCurrentUser().getUid();
                                System.out.println(userID);
                                writeNewUser(userID,Name, FamilyName, Tel, Email, pwd);

                                Toast.makeText(CreateAccountActivity.this, "User Registered..", Toast.LENGTH_SHORT).show();
                                startMainActivity();
                                finish();
                            } else {

                                // in else condition we are displaying a failure toast message.
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(CreateAccountActivity.this, "Fail to register user..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void writeNewUser(String userId, String name, String familyName, String telNumber, String email, String password) {
        User user = new User(userId, name, familyName, telNumber, email, password);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userId).setValue(user);
    }


    private void startMainActivity() {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}

