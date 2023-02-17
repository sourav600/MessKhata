package com.example.messbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setStatusBarColor(ContextCompat.getColor(SplashScreen.this,R.color.appColor));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.preferenceName,0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);

                SharedPreferences sharedPreferences2 = getSharedPreferences(LoginAsMemberActivity.preferenceName2,0);
                boolean hasLoggedIn2 = sharedPreferences2.getBoolean("hasLoggedIn",false);

                if(hasLoggedIn){
                    Intent intent = new Intent(SplashScreen.this, NavigationActivity.class);
                    startActivity(intent);
                    finish();
                }else if(hasLoggedIn2){
                    startActivity(new Intent(SplashScreen.this, NavigationActivityForMember.class));
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}