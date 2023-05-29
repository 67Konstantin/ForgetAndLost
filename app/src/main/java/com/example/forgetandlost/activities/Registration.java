package com.example.forgetandlost.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forgetandlost.helperClasses.HelperClassUsers;
import com.example.forgetandlost.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registration extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView tv2;
    EditText etEmail, etPassword, etname;
    Button btReg;
    CheckBox checkBox;
    Vibrator vibrator;
    int mls = 100, x = 0;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase dataBase;
    DatabaseReference reference;
    View layer1;
    String uid, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(Registration.this, List.class));
        }
        {
            etEmail = findViewById(R.id.editText_gmail);
            etname = findViewById(R.id.editText_name);
            etPassword = findViewById(R.id.editText_password);
            checkBox = findViewById(R.id.checkBox);
            btReg = findViewById(R.id.btRegisterEnd);
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            linearLayout = findViewById(R.id.lineal);
            tv2 = findViewById(R.id.tv2);
            LayoutInflater inflater = getLayoutInflater();

            layer1 = inflater.inflate(R.layout.layer1, null);
        }
        SpannableString ss = new SpannableString("Даю согласие на обработку \n " + "  " + "персональных данных");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Registration.this, ProgressBar.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 4, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBox.setText(ss);
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
        checkBox.setHighlightColor(Color.TRANSPARENT);
    }

    public void SingInGoogle(View view) {
        startActivity(new Intent(Registration.this, Login.class));
    }

    public Boolean isFieldRight() {
        if (checkEmail(etEmail)) {
            etEmail.setError(null);

            if (etname.getText().toString().length() != 0) {
                etname.setError(null);

                if (password.length() >= 6) {
                    etPassword.setError(null);
                    if (!password.matches("(.*) (.*)")) {
                        if (checkBox.isChecked()) {
                            return true;
                        } else {
                            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                            checkBox.setAnimation(shake);
                            checkBox.setDrawingCacheBackgroundColor(Color.RED);
                            vibrator.vibrate(mls);
                            return false;
                        }

                    } else {
                        etPassword.setError("Пароль не должен содержать пробелы");
                        vibrator.vibrate(mls);
                        return false;
                    }

                } else {
                    etPassword.setError("Пароль должен содержать не менее 6 символов");
                    vibrator.vibrate(mls);
                    return false;
                }
            } else {
                etname.setError("Вы не ввели имя");
                vibrator.vibrate(mls);
                return false;
            }
        } else {
            etEmail.setError("Некорректно введена почта ");
            vibrator.vibrate(mls);
            return false;
        }

    }

    public void RegClick(View view) {
        if (isNetworkConnected()) {

            String email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            String name = etname.getText().toString();

            if (x == 0) {

                if (isFieldRight()) {


                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showToast("Пользователь с этой почтой уже существует и вы ввели правильный пароль");
                            startActivity(new Intent(Registration.this, List.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        dataBase = FirebaseDatabase.getInstance();
                                        reference = dataBase.getReference("users");
                                        uid = FirebaseAuth.getInstance().getUid();
                                        HelperClassUsers helperClass = new HelperClassUsers(email, name, uid, "https://firebasestorage.googleapis.com/v0/b/forgetandlost-d5238.appspot.com/o/images%2F70388%2F70388?alt=media&token=036a892f-bc27-463f-b26d-f33ca35227cc");
                                        reference.child(uid).setValue(helperClass);
                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    linearLayout.addView(layer1);
                                                    setMargins(tv2, 18, 300, 18, 10);
                                                    showToast("Письмо было отправлено на почту " + email);
                                                    btReg.setText("Проверить Верификацию");
                                                    x = 1;
                                                } else {
                                                    showToast("Не удалось отправить письмо");
                                                }
                                            }
                                        });
                                    } else {
                                        showToast("Пользователь с этой почтой уже зарегестрирован");
                                    }
                                }
                            });
                        }
                    });


                }
            } else if (x == 1) {
                FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                            showToast("Вы успешно подтвердили почту");
                            startActivity(new Intent(Registration.this, List.class));
                        } else {
                            showToast("Вы не подтвердили почту");
                        }
                    }
                });
            }

        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btRegisterEnd), "Подключение к интернету отсутсвует", Snackbar.LENGTH_LONG);
            view = snackbar.getView();
            TextView textView = view.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
        }

    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    boolean checkEmail(EditText email) {
        String emailSt = email.getText().toString();
        if (!emailSt.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailSt).matches()) {
            return true;
        } else {
            return false;
        }
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}