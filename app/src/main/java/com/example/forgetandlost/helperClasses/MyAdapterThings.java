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

public class MyAdapterThings extends RecyclerView.Adapter<MyViewHolderThings> {

    private Context context;
    private List<HelperClassThings> dataList;

    public MyAdapterThings(Context context, List<HelperClassThings> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderThings onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_things, parent, false);
        return new MyViewHolderThings(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderThings holder, int position) {
        String name = dataList.get(position).getName();
        String describing = dataList.get(position).getDescribing();
        String data = dataList.get(position).getData().substring(0, dataList.get(position).getData().indexOf(","));
        String userId = dataList.get(position).getUserId();
        String conditions = dataList.get(position).getConditions();
        String area = dataList.get(position).getArea();

        Glide.with(context).load(dataList.get(position).getImage()).into(holder.listImage);
        holder.listName.setText(name);
        holder.listDescribing.setText(describing);
        holder.listData.setText(data);


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("image", dataList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("userId", userId);
                intent.putExtra("name", name);
                intent.putExtra("describing", describing);
                intent.putExtra("data", data);
                intent.putExtra("area", area);
                intent.putExtra("conditions", conditions);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<HelperClassThings> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolderThings extends RecyclerView.ViewHolder {

    ImageView listImage;
    TextView listName, listDescribing, listData;
    CardView recCard;

    public MyViewHolderThings(@NonNull View itemView) {
        super(itemView);

        listImage = itemView.findViewById(R.id.listImageThings);
        recCard = itemView.findViewById(R.id.recCardThings);
        listDescribing = itemView.findViewById(R.id.listDescribingThings);
        listData = itemView.findViewById(R.id.listTimeThings);
        listName = itemView.findViewById(R.id.listNameThings);
    }
}