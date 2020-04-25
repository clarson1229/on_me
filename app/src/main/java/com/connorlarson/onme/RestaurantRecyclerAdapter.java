package com.connorlarson.onme;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.RestaurantRecyclerAdapterViewHolder> {
    private Restaurant[] restaurantArray;

    public static class RestaurantRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView resName;
        public TextView resAddress;

        public RestaurantRecyclerAdapterViewHolder (View itemView){
            super(itemView);
            resName= itemView.findViewById(R.id.resNameCardTextView);
            resAddress = itemView.findViewById(R.id.resaddressCardTextView);
        }
    }

    public RestaurantRecyclerAdapter (Restaurant[] rArray){
        // converts hashMap into arrayList of just the restaurants.
        restaurantArray = rArray;
        Log.d("recycleConstructor:", restaurantArray.toString());
    }

    @NonNull
    @Override
    public RestaurantRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        RestaurantRecyclerAdapterViewHolder RRAVH = new RestaurantRecyclerAdapterViewHolder(v);
        return RRAVH;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantRecyclerAdapterViewHolder holder, int position) {
        Restaurant currentItem = restaurantArray[position];
        holder.resName.setText(currentItem.getResName());
        holder.resAddress.setText(currentItem.getResAddress());
    }

    @Override
    public int getItemCount() {
        return restaurantArray.length;
    }




}