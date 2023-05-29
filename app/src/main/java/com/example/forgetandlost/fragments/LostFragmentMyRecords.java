package com.example.forgetandlost.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LostFragmentMyRecords extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public static final String TITLE = "Подарки";

    public LostFragmentMyRecords() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<HelperClassThings> dataList;
    MyAdapterThings adapter;
    SearchView searchView;
    private ALodingDialog aLodingDialog;
    FirebaseUser user;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lost_my_records, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMyLost);
        searchView = view.findViewById(R.id.searchMyLost);
        back = view.findViewById(R.id.imageBack2);
        searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        user = FirebaseAuth.getInstance().getCurrentUser();
        aLodingDialog = new ALodingDialog(getActivity());
        aLodingDialog.show();
        Handler handler = new Handler();
        Runnable runnable = () -> aLodingDialog.cancel();
        handler.postDelayed(runnable, 400000);
        dataList = new ArrayList<>();
        adapter = new MyAdapterThings(getActivity(), dataList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("things").child("Находка").child(user.getUid());
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                HashMap<String, HelperClassThings> hashMap = (HashMap<String, HelperClassThings>) snapshot.getValue();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    HelperClassThings helperClassThings = ds.getValue(HelperClassThings.class);
                    dataList.add(helperClassThings);
                }
                adapter.notifyDataSetChanged();
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
        back.setOnClickListener(view1 -> getActivity().onBackPressed());
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