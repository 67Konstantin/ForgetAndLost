package com.example.forgetandlost.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.forgetandlost.helperClasses.PageViewAdapter;
import com.example.forgetandlost.R;
import com.google.android.material.tabs.TabLayout;

public class MyRecords extends AppCompatActivity {
    SharedPreferences sPref;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_records);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Потеряшки"));
        tabLayout.addTab(tabLayout.newTab().setText("Отдам Даром"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final PageViewAdapter adapter = new PageViewAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

}