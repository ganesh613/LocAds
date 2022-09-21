package com.example.locationbasedads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private List<MenuModel> menuList;



    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, cost;
        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
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
        MenuModel movie = menuList.get(position);
        holder.title.setText(movie.getTitle());
        holder.cost.setText(movie.getCost());
    }
    @Override
    public int getItemCount() {
        return menuList.size();
    }
}