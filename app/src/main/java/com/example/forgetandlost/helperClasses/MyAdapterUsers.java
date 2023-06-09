package com.example.forgetandlost.helperClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.forgetandlost.R;
import com.example.forgetandlost.activities.DetailActivity;
import com.example.forgetandlost.fragments.MessageFragment;
import com.example.forgetandlost.fragments.UsersMessagesFragment;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterUsers extends RecyclerView.Adapter<MyViewHolderUsers> {

    private Context context;
    private List<HelperClassUsers> dataList;

    public MyAdapterUsers(Context context, List<HelperClassUsers> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_users, parent, false);
        return new MyViewHolderUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderUsers holder, int position) {
        String name = dataList.get(position).getName();
        String email = dataList.get(position).getEmail();

        Glide.with(context).load(dataList.get(position).getImage()).into(holder.listImage);
        holder.listName.setText(name);
        holder.listEmail.setText(email);

        holder.recCard.setOnClickListener(view -> {
            replaceFragment(new MessageFragment(),context);
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<HelperClassUsers> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
    public void replaceFragment(Fragment fragment, Context context) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

class MyViewHolderUsers extends RecyclerView.ViewHolder {

    ImageView listImage;
    TextView listName, listEmail;
    CardView recCard;

    public MyViewHolderUsers(@NonNull View itemView) {
        super(itemView);

        listImage = itemView.findViewById(R.id.listImageUsers);
        recCard = itemView.findViewById(R.id.recCardUsers);
        listEmail = itemView.findViewById(R.id.listEmailUsers);
        listName = itemView.findViewById(R.id.listNameUsers);
    }

}