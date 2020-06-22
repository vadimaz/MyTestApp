package com.blogspot.vadimaz.mytestapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView id, name, email;
    private ImageView image;

    public static final String TAG = "test_app";
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    AccessToken accessToken;
    Profile profile;
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken != null) {
                loadUserProfile(currentAccessToken);
            } else {
                eraseUserProfile();
            }
        }
    };

    ProfileTracker profileTracker = new ProfileTracker() {
        @Override
        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            if (currentProfile != null) {
                Log.d(TAG, currentProfile.getId());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = findViewById(R.id.id);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        image = findViewById(R.id.image);
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
    }

    private void loadUserProfile(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String f = object.getString("first_name");
                    String l = object.getString("last_name");
                    String e = object.getString("email");
                    String i = object.getString("id");
                    String u = "https://graph.facebook.com/" + i + "/picture?type=normal";
                    Log.d(TAG, String.format("\nFirst name: %s\nLast name: %s\nEmail: %s\nId: %s\nImage URL: %s", f, l, e, i, u));

                    id.setText(i);
                    name.setText(String.format("%s %s", f, l));
                    email.setText(e);
                    Glide.with(MainActivity.this).load(u).into(image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void eraseUserProfile() {
        id.setText("");
        name.setText("");
        email.setText("");
        Glide.with(this).clear(image);
    }

    public void showDialog(View v) {
                MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(TAG, "send success");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "send cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "send error");
            }
        });

        Uri uri = Uri.parse("http://developer.android.com/reference/android/net/Uri.html");
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("....")
                .setContentDescription("asd")
                .setContentUrl(uri)
                .setImageUrl(Uri.parse("http://www.w3schools.com/css/paris.jpg"))
                .build();
        messageDialog.show(linkContent);
    }
}
