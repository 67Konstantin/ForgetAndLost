package com.example.forgetandlost.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.forgetandlost.R;
import com.example.forgetandlost.helperClasses.HelperClassThings;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    TextView etdata;
    DatabaseReference reference;
    EditText etName, etdescribing, etconditions;
    AutoCompleteTextView etarea;
    String name;
    String describing;
    String image;
    String userId;
    String conditions;
    String area;
    String data;
    String key;
    ImageView detailImage, back;
    RelativeLayout relativeLayout;
    View relativeLayout1;
    FloatingActionButton deleteButton, editButton;
    String imageURL;
    private Uri filePath;
    boolean x = true;
    int mls = 100;
    Vibrator vibrator;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    boolean changeImage = false;
    String[] areas;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //принимаю интент
        {
            Intent intent = getIntent();
            name = intent.getStringExtra("name");
            describing = intent.getStringExtra("describing");
            image = intent.getStringExtra("image");
            userId = intent.getStringExtra("userId");
            conditions = intent.getStringExtra("conditions");
            area = intent.getStringExtra("area");
            data = intent.getStringExtra("data");
            key = intent.getStringExtra("key");
        }
        //findViewById
        {
            areas = getResources().getStringArray(R.array.areas);
            etarea = findViewById(R.id.detailArea);
            back = findViewById(R.id.backDetails);
            etName = findViewById(R.id.detailName);
            etarea = findViewById(R.id.detailArea);
            etconditions = findViewById(R.id.detailConditions);
            etdata = findViewById(R.id.detailData);
            etdescribing = findViewById(R.id.detaildescribing);
            detailImage = findViewById(R.id.detailImage);
            back.setOnClickListener(view -> onBackPressed());
            relativeLayout = findViewById(R.id.rlDetail);
            relativeLayout1 = getLayoutInflater().inflate(R.layout.floating_bar_detail_my, null);
        }
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        etName.setEnabled(false);
        etdescribing.setEnabled(false);
        etconditions.setEnabled(false);
        etarea.setEnabled(false);
        detailImage.setEnabled(false);
        //ставлю на свои места
        {
            etName.setText(name);
            etdescribing.setText(describing);
            etconditions.setText(conditions);
            etdata.setText(data);
            etarea.setText(area);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference getImage = firebaseDatabase.getReference().child("image");
            getImage.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Glide.with(DetailActivity.this).load(image).into(detailImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    showToast("Не удалось загрузить изображение");
                }
            });
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, areas);
        etarea.setAdapter(adapter);
        detailImage.setOnClickListener(view -> {
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
        reference = FirebaseDatabase.getInstance().getReference("things");
        if (Objects.equals(userId, FirebaseAuth.getInstance().getUid())) {
            relativeLayout.addView(relativeLayout1);
            deleteButton = relativeLayout1.findViewById(R.id.deleteButton);
            editButton = relativeLayout1.findViewById(R.id.editButton);
            deleteButton.setOnClickListener(view -> {
                reference.child("Находка").child(FirebaseAuth.getInstance().getUid()).child(key).removeValue();
                reference.child("Отдам даром").child(FirebaseAuth.getInstance().getUid()).child(key).removeValue();
                Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                onBackPressed();
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (x) {
                        etName.setEnabled(true);
                        etdescribing.setEnabled(true);
                        etconditions.setEnabled(true);
                        etarea.setEnabled(true);
                        detailImage.setEnabled(true);
                        showToast("Теперь вы можете изменить содержимое полей");
                        editButton.setImageResource(R.drawable.baseline_check_circle_outline_24);
                        x = false;
                    } else {
                        if (rightValue()) {
                            editButton.setImageResource(R.drawable.baseline_edit_24);
                            if (changeImage) {
                                image = imageURL;
                            }
                            HelperClassThings thing = new HelperClassThings(etName.getText().toString(), etdescribing.getText().toString(), etconditions.getText().toString(),
                                    etarea.getText().toString(), data, userId, image, key);
                            FirebaseDatabase.getInstance().getReference("things").child("Находка").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String ooo = String.valueOf(snapshot.child(key).child("image").getValue());
                                    if (ooo == "null") {
                                        reference.child("Отдам даром").child(userId).child(key).setValue(thing);
                                    } else {
                                        reference.child("Находка").child(userId).child(key).setValue(thing);
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            etName.setEnabled(false);
                            etdescribing.setEnabled(false);
                            etconditions.setEnabled(false);
                            etarea.setEnabled(false);
                            detailImage.setEnabled(false);

                            changeImage = false;
                            x = true;
                        }
                    }
                }
            });
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            filePath = data.getData();
            detailImage.setImageURI(filePath);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(filePath.getLastPathSegment());
            storageReference.child(filePath.getLastPathSegment()).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    imageURL = uriTask.getResult().toString();
                    changeImage = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast("Не удалось загрузить изображение");
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    showToast("Необходимо предоставить доступ к галлереи");
                }
            }
        }
    }

    private boolean rightValue() {
        if (etName.getText().toString().trim().equals("") && etarea.getText().toString().trim().equals("") && etconditions.getText().toString().trim().equals("") && etdescribing.getText().toString().trim().equals("")) {
            showToast("Одно из полей пустое");
            vibrator.vibrate(mls);
            return false;
        } else if (!(Arrays.asList(areas).contains(etarea.getText().toString()))) {
            showToast("Неверно введена область");
            vibrator.vibrate(mls);
            return false;
        } else return true;
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}