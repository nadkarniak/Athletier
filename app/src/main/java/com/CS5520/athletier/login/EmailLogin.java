package com.CS5520.athletier.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CS5520.athletier.MainActivity;
import com.CS5520.athletier.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailLogin extends AppCompatActivity {
    EditText  username, password;
    Button register, login;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String email = username.getText().toString();
                                            String pwd = password.getText().toString();
                                            if (email.isEmpty()) {
                                                username.setError("Please enter an email to register!");
                                                username.requestFocus();
                                            } else if (pwd.isEmpty()) {
                                                password.setError("Please enter a password to register!");
                                                password.requestFocus();
                                            } else if (email.isEmpty() && pwd.isEmpty()) {
                                                Toast.makeText(EmailLogin.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                                            } else if (!(email.isEmpty() && pwd.isEmpty())) {
                                                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(EmailLogin.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(EmailLogin.this, "There are some errors while creating your account, please try again.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            //move to profile
                                                            //TODO check the link connection
                                                            startActivity(new Intent(EmailLogin.this, MainActivity.class));
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(EmailLogin.this, "Error occured, please try again.", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                    });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        String email = username.getText().toString();
                        String pwd = password.getText().toString();

                        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                        if (mFirebaseUser != null) {
                            Toast.makeText(EmailLogin.this, "You are logged in.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EmailLogin.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(EmailLogin.this, "Please try again.", Toast.LENGTH_SHORT).show();
                        }

                        if (email.isEmpty()) {
                            username.setError("Please enter an email to login!");
                            username.requestFocus();
                        } else if (pwd.isEmpty()) {
                            password.setError("Please enter a password to login!");
                            password.requestFocus();
                        } else if (email.isEmpty() && pwd.isEmpty()) {
                            Toast.makeText(EmailLogin.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                        } else if (!(email.isEmpty() && pwd.isEmpty())) {
                            mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(EmailLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(EmailLogin.this, "Error occured, please try again.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //TODO redirect to profile
                                        Intent profilepage = new Intent(EmailLogin.this, MainActivity.class);
                                        startActivity(profilepage);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(EmailLogin.this, "Error occured, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            }
        });
    }
}
