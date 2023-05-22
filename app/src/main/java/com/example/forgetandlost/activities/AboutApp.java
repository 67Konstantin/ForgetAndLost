package com.example.forgetandlost.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.forgetandlost.R;

public class AboutApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
    }

    public void LLL(View view) {
        onBackPressed();
    }
}