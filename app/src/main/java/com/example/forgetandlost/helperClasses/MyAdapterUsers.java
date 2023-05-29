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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.forgetandlost.R;
import com.example.forgetandlost.activities.DetailActivity;

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
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("image", dataList.get(holder.getAdapterPosition()).getImage());
            context.startActivity(intent);
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