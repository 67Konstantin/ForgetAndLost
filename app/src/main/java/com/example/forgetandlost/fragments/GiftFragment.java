package com.example.forgetandlost.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forgetandlost.R;
import com.example.forgetandlost.helperClasses.ALodingDialog;
import com.example.forgetandlost.helperClasses.HelperClassThings;
import com.example.forgetandlost.helperClasses.MyAdapterThings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class GiftFragment extends Fragment {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<HelperClassThings> dataList;
    MyAdapterThings adapter;
    SearchView searchView;
    ALodingDialog aLodingDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.search);
        searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        aLodingDialog = new ALodingDialog(getActivity());
        aLodingDialog.show();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                aLodingDialog.cancel();
            }
        };
        handler.postDelayed(runnable, 5000);


        dataList = new ArrayList<>();
        adapter = new MyAdapterThings(getActivity(), dataList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("things").child("Отдам даром");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                HashMap<String, HelperClassThings> hashMap = (HashMap<String, HelperClassThings>) snapshot.getValue();
                try {
                    for (String s : hashMap.keySet()) {
                        if (!(Objects.equals(s, FirebaseAuth.getInstance().getUid()))) {
                            for (DataSnapshot ds : snapshot.child(s).getChildren()) {
                                HelperClassThings helperClassThings = ds.getValue(HelperClassThings.class);
                                dataList.add(helperClassThings);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception ignored) {
                }
                aLodingDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                aLodingDialog.cancel();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        return view;
    }

    public void searchList(String text) {
        ArrayList<HelperClassThings> searchList = new ArrayList<>();
        for (HelperClassThings dataClass : dataList) {
            if (dataClass.getName().toLowerCase().contains(text.toLowerCase()) || dataClass.getDescribing().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
}