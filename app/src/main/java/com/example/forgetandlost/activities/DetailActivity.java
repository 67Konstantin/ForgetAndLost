package com.example.forgetandlost.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.forgetandlost.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    TextView etName, etdescribing, etconditions, etarea, etdata;
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
                    Toast.makeText(DetailActivity.this, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show();
                }
            });
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("things");
        FirebaseStorage storage = FirebaseStorage.getInstance();
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
        }


    }
}