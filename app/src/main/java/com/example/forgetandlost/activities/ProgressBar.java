package com.example.forgetandlost.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.forgetandlost.R;

public class ProgressBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
    }

    public void Click(View view) {
        onBackPressed();
    }
}