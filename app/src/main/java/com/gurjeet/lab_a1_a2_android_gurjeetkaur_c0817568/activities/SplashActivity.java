package com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gurjeet.lab_a1_a2_android_gurjeetkaur_c0817568.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final int SPLASH_DISPLAY_LENGTH = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);//moving to second activity page of providerList
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}