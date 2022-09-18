package com.example.locationbasedads;

import android.graphics.Picture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private List<MenuModel> menuList;
    ImageView img;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, cost;
       // ImageView img;
        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            img = view.findViewById(R.id.rImage);
            cost = view.findViewById(R.id.cost);
        }
    }
    public MenuAdapter(List<MenuModel> menuList) {
        this.menuList = menuList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        Glide.with(holder.itemView.getContext())
//                .load(pi.())
//                .into(holder.picture);
        MenuModel movie = menuList.get(position);
        holder.title.setText(movie.getTitle());
        Glide.with(img.getContext())
                .load(movie.getImg())
                        .into(img);
//        holder.img.(movie.getImg());
        holder.cost.setText(movie.getCost());
    }
    @Override
    public int getItemCount() {
        return menuList.size();
    }
}