package com.example.forgetandlost.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.forgetandlost.R;
import com.example.forgetandlost.activities.List;
import com.example.forgetandlost.databinding.ActivityListActivtyBinding;

public class MessageFragment extends Fragment {
    ImageView ivBack ;
    FrameLayout flSend;
    EditText etInputMessage;
    View viewBackground;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ActivityListActivtyBinding bindingList = List.bindingList;
        bindingList.fab.setVisibility(View.INVISIBLE);
        {
            ivBack = view.findViewById(R.id.imageBack);
            flSend = view.findViewById(R.id.layoutSend);
            etInputMessage = view.findViewById(R.id.inputMessage);
        }
        ivBack.setOnClickListener(view1 -> {
            bindingList.bottomNavigationView.findViewById(R.id.messages).performClick();
        });
        flSend.setOnClickListener(view1 -> {
            etInputMessage.setText("");
        });
        return view;
    }



}
