package com.blogspot.vadimaz.mytestapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "test_app";
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    AccessToken accessToken;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton loginButton = findViewById(R.id.login_button);
        //CallbackManager callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions("email");
        //loginButton.setFragment(this); //if fragment

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "error");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Log.d(TAG, "User logged in: " + isLoggedIn);

        profile = Profile.getCurrentProfile();
        boolean profileAvailable = profile != null;
        Log.d(TAG, "Profile is available: " + profileAvailable);

        if (!isLoggedIn) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
    }
}
