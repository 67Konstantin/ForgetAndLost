package com.example.forgetandlost.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forgetandlost.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    EditText loginUserEmail, loginPassword;
    Button btLoginEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        {
            loginUserEmail = findViewById(R.id.editText_gmail_login);
            loginPassword = findViewById(R.id.et_password_login);
            btLoginEnd = findViewById(R.id.btLoginEnd);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
    }

    public void GoToReg(View view) {
        startActivity(new Intent(Login.this, Registration.class));
    }


    public void checkUser() {
        String username = loginUserEmail.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("userName").equalTo(username);


        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    loginUserEmail.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {
                        loginUserEmail.setError(null);
                        startActivity(new Intent(Login.this, List.class));
                    } else {
                        loginPassword.setError("Неверный пароль");
                        loginUserEmail.requestFocus();
                    }
                } else {
                    loginUserEmail.setError("Пользователя с такой почтой не существует");
                    loginUserEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LogClick(View view) {
        if (isNetworkConnected()){
        if (checkEmail(loginUserEmail)) {
            if (loginPassword.getText().toString().length() >= 6) {
                if (firebaseAuth.getCurrentUser() != null) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    showToast( user.getEmail() + ", вы уже вошли!");
                } else {

                    firebaseAuth.signInWithEmailAndPassword(loginUserEmail.getText().toString(), loginPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User signed in successfully
                                        showToast("вход выполнен");
                                        startActivity(new Intent(Login.this, List.class));
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast("ошибка входа");
                                }
                            });
                }
                } else{
                    loginPassword.setError("Пароль содержит не менее 6 символов");
                }
            } else {
                loginUserEmail.setError("Неверно введена почта");

        }} else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btLoginEnd), "Подключение к интернету отсутсвует", Snackbar.LENGTH_LONG);
            view = snackbar.getView();
            TextView textView = view.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }
    }

    boolean checkEmail(EditText email) {
        String emailSt = email.getText().toString();
        if (!emailSt.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailSt).matches()) {
            return true;
        } else {
            return false;
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}