package com.example.forgetandlost.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.forgetandlost.R;
import com.example.forgetandlost.activities.AboutApp;
import com.example.forgetandlost.activities.MyRecords;
import com.example.forgetandlost.activities.Registration;
import com.example.forgetandlost.activities.SettingsProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;


public class ProfileFragment extends Fragment {
    LinearLayout btSingOut;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FrameLayout flImageProfile;
    ImageView ivImageProfile;
    TextView tvNameProfile, tvEmailProfile, tvSettingsProfile, tvMyRecords, tvAboutApp;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri filePath;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        {
            user = firebaseAuth.getCurrentUser();
            flImageProfile = view.findViewById(R.id.flImageProfile);
            ivImageProfile = view.findViewById(R.id.ivImageProfile);
            tvNameProfile = view.findViewById(R.id.tvNameProfile);
            tvEmailProfile = view.findViewById(R.id.tvEmailProfile);
            tvSettingsProfile = view.findViewById(R.id.tvSettingsProfile);
            tvAboutApp = view.findViewById(R.id.tvAboutApp);
            tvMyRecords = view.findViewById(R.id.tvMyRecords);
            btSingOut = view.findViewById(R.id.exit_logout);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        }
        tvAboutApp.setOnClickListener(view1 -> startActivity(new Intent(getContext(), AboutApp.class)));
        try {
            databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String url = (String) snapshot.child("image").getValue();
                    String name = (String) snapshot.child("name").getValue();
                    String email = (String) snapshot.child("email").getValue();

                    try {
                        Glide.with(getActivity()).load(url).into(ivImageProfile);
                    } catch (Exception ignored) {
                    }
                    tvNameProfile.setText(name);
                    tvEmailProfile.setText(email);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception ignored) {        }

        flImageProfile.setOnClickListener(view1 -> {
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
        btSingOut.setOnClickListener(view12 -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), Registration.class));
        });
        tvSettingsProfile.setOnClickListener(view13 -> startActivity(new Intent(getContext(), SettingsProfile.class)));
        tvMyRecords.setOnClickListener(view14 -> startActivity(new Intent(getActivity(), MyRecords.class)));
        return view;
    }

    private int checkSelfPermission(String readExternalStorage) {
        return 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            filePath = data.getData();
            ivImageProfile.setImageURI(filePath);
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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}