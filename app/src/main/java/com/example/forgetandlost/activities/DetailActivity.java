package com.example.forgetandlost.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.forgetandlost.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    TextView etName, etdescribing, etconditions, etarea, etdata;
    ;
    String name;
    String describing;
    String image;
    String userId;
    String conditions;
    String area;
    String data;
    ImageView detailImage, back;

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
        }
        //findViewById


        back = findViewById(R.id.backDetails);
        etName = findViewById(R.id.detailName);
        etarea = findViewById(R.id.detailArea);
        etconditions = findViewById(R.id.detailConditions);
        etdata = findViewById(R.id.detailData);
        etdescribing = findViewById(R.id.detaildescribing);
        detailImage = findViewById(R.id.detailImage);
        back.setOnClickListener(view -> onBackPressed());
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

    }
}