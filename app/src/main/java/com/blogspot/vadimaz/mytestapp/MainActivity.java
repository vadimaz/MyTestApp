package com.blogspot.vadimaz.mytestapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton loginButton = findViewById(R.id.login_button);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions("email");
        //loginButton.setFragment(this);
    }
}
