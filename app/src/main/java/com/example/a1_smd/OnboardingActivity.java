package com.example.a1_smd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button getStarted = findViewById(R.id.btnGetStarted);

        getStarted.setOnClickListener(v -> {

            Intent intent = new Intent(OnboardingActivity.this, HomepageActivity.class);
            startActivity(intent);

            finish();
        });
    }
}