package com.example.forgetandlost.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.forgetandlost.R;
import com.example.forgetandlost.databinding.ActivityListActivtyBinding;
import com.example.forgetandlost.helperClasses.ALodingDialog;
import com.example.forgetandlost.helperClasses.HelperClassUsers;
import com.example.forgetandlost.helperClasses.MyAdapterUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersMessagesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UsersMessagesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    List<HelperClassUsers> dataList;
    MyAdapterUsers adapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    SearchView searchView;
    ImageView ivMyProfile;
    private ALodingDialog aLodingDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_messages, container, false);
        {
            recyclerView = view.findViewById(R.id.recyclerViewUsersMessages);
            searchView = view.findViewById(R.id.searchViewUsers);
            ivMyProfile = view.findViewById(R.id.imageMyProfile);
        }
        ActivityListActivtyBinding bindingList = com.example.forgetandlost.activities.List.bindingList;
        bindingList.fab.setVisibility(View.VISIBLE);
        try {
            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String url = (String) snapshot.child("image").getValue();
                    try {
                        Glide.with(getActivity()).load(url).into(ivMyProfile);
                    } catch (Exception ignored) {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception ignored) {

        }
        searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        aLodingDialog = new ALodingDialog(getActivity());
        aLodingDialog.show();
        Handler handler = new Handler();
        Runnable runnable = () -> aLodingDialog.cancel();
        handler.postDelayed(runnable, 400000);
        dataList = new ArrayList<>();
        adapter = new MyAdapterUsers(getActivity(), dataList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (!(Objects.equals(ds, FirebaseAuth.getInstance().getUid()))) {
                            HelperClassUsers helperClassUsers = ds.getValue(HelperClassUsers.class);
                            dataList.add(helperClassUsers);
                        }
                        adapter.notifyDataSetChanged();
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
        ArrayList<HelperClassUsers> searchList = new ArrayList<>();
        for (HelperClassUsers dataClass : dataList) {
            if (dataClass.getName().toLowerCase().contains(text.toLowerCase()) || dataClass.getEmail().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }


}