package com.example.forgetandlost.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.forgetandlost.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingsProfile extends AppCompatActivity {
    FrameLayout flImageProfileRedact;
    ImageView ivImageProfileRedact, back;
    EditText etProfileNameRedact;
    TextView tvProfileEmailRedact, tvChangePasswordRedact, etPasswordRedact;
    String uid;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    DatabaseReference databaseReference;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        {
            btSave = findViewById(R.id.btSave);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            user = firebaseAuth.getCurrentUser();
            flImageProfileRedact = findViewById(R.id.flImageProfileRedact);
            ivImageProfileRedact = findViewById(R.id.ivImageProfileRedact);
            etProfileNameRedact = findViewById(R.id.etProfileNameRedact);
            tvProfileEmailRedact = findViewById(R.id.tvProfileEmailRedact);
            etPasswordRedact = findViewById(R.id.etPasswordRedact);
            tvChangePasswordRedact = findViewById(R.id.tvChangePasswordRedact);
            back = findViewById(R.id.backSettings);
        }
        btSave.setOnClickListener(view -> {
            databaseReference.child(user.getUid()).child("name").setValue(etProfileNameRedact.getText().toString());
        });

        String r = etProfileNameRedact.getText().toString();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etProfileNameRedact.getText().toString().length() != 0 && !etProfileNameRedact.getText().toString().equals(r)) {
                    btSave.setEnabled(true);
                } else btSave.setEnabled(false);
            }
        };
        etProfileNameRedact.addTextChangedListener(textWatcher);

        ivImageProfileRedact.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    pickImageFromGallery();
                }
            } else {
                pickImageFromGallery();
            }
        });
        back.setOnClickListener(view -> onBackPressed());
        try {
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String url = (String) snapshot.child("image").getValue();
                    try {
                        Glide.with(SettingsProfile.this).load(url).into(ivImageProfileRedact);
                    } catch (Exception ignored) {

                    }
                    etProfileNameRedact.setText((String) snapshot.child("name").getValue());
                    tvProfileEmailRedact.setText(user.getEmail());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception ignored) {
        }
        tvChangePasswordRedact.setOnClickListener(view -> {
            FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail());
            showToast("На вашу почту было отправленно письмо для изменения пароля");
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Uri filePath = data.getData();
            ivImageProfileRedact.setImageURI(filePath);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(filePath.getLastPathSegment());

            storageReference.child(filePath.getLastPathSegment()).putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                String imageURL = uriTask.getResult().toString();
                databaseReference.child(user.getUid()).child("image").setValue(imageURL);
            }).addOnFailureListener(e -> showToast("Не удалось загрузить изображение"));
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    public void showToast(String message) {
        Toast.makeText(SettingsProfile.this, message, Toast.LENGTH_SHORT).show();
    }
}