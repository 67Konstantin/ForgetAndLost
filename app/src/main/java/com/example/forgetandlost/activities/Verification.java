package com.example.forgetandlost.activities;


import static com.example.forgetandlost.activities.Registration.APP_PREFERENCES;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forgetandlost.R;
import com.example.forgetandlost.helperClasses.HelperClassUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Verification extends AppCompatActivity {
    Button btSend, btCheck, btLogOut;
    FirebaseUser user;
    String name, email, uid;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        user = FirebaseAuth.getInstance().getCurrentUser();
        btSend = findViewById(R.id.btSendEmail);
        btCheck = findViewById(R.id.btCheckVerification);
        btLogOut = findViewById(R.id.btLogOut);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        name = mSettings.getString(Registration.APP_PREFERENCES_NAME, "");
        email = mSettings.getString(Registration.APP_PREFERENCES_EMAIL, "");
        uid = user.getUid();
    }

    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Verification.this, Registration.class));
    }

    public void Check(View view) {


        FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(task -> {
            if (user.isEmailVerified()) {
                showToast("Вы успешно подтвердили почту");
                HelperClassUsers helperClass = new HelperClassUsers(email, name, uid, "https://firebasestorage.googleapis.com/v0/b/forgetandlost-d5238.appspot.com/o/images%2F70388%2F70388?alt=media&token=036a892f-bc27-463f-b26d-f33ca35227cc");
                FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(helperClass);
                startActivity(new Intent(Verification.this, List.class));
            } else {
                showToast("Вы не подтвердили почту");
            }
        }).addOnFailureListener(e -> showToast("Вы не подтвердили почту"));
    }

    public void Send(View view) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("Письмо было отправлено на вашу почту");
            } else {
                showToast("Не удалось отправить письмо, попробуйте позже");
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