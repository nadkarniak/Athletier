package com.CS5520.athletier.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CS5520.athletier.MainActivity;
import com.CS5520.athletier.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class FacebookLogin extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static final String TAG = "FACELOG";
    private LoginButton mFacebookBtn;
    private TextView mTextView;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            if (mUser == null) {
                setContentView(R.layout.activity_login);
                FacebookSdk.sdkInitialize(getApplicationContext());
                mFacebookBtn = (LoginButton) findViewById(R.id.fblogin);
                mCallbackManager = CallbackManager.Factory.create();
                mFacebookBtn.setReadPermissions(Arrays.asList("email"));
                mTextView = (TextView)findViewById(R.id.textView);
            } else {
                Intent accountIntent = new Intent(FacebookLogin.this, MainActivity.class);
                startActivity(accountIntent);
            }
        }
            public void buttonClicked(View v)
            {
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>(){
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }


                    // Initialize Facebook Login button


                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });
            }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FacebookLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


        }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);

        }
    }
    //TODO:Link login page with profile page
    private void updateUI(FirebaseUser currentUser){
            //after login, move to profile page
       mTextView.setText(currentUser.getEmail());
        finish();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
