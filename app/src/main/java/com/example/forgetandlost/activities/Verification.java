package com.example.forgetandlost.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forgetandlost.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class Verification extends AppCompatActivity {
    Button btSend, btCheck, btLogOut;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        btSend = findViewById(R.id.btSendEmail);
        btCheck = findViewById(R.id.btCheckVerification);
        btLogOut = findViewById(R.id.btLogOut);
    }
    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Verification.this, Registration.class));
    }
    public void Check(View view) {

        user =FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(task -> {
            showToast( "Вы успешно подтвердили почту");
            startActivity(new Intent(Verification.this, List.class));
        }).addOnFailureListener(e -> showToast("Вы не подтвердили почту"));
    }

    public void Send(View view) {

        user =FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("Письмо было отправлено на вашу почту");
            } else {
                showToast( "Не удалось отправить письмо, попробуйте позже");
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}