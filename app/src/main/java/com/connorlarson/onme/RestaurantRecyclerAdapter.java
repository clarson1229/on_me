package com.connorlarson.onme;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.RestaurantRecyclerAdapterViewHolder> {
    private ArrayList<Restaurant> restaurantArray = new ArrayList<Restaurant>();

    public static class RestaurantRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView resName;
        public TextView resAddress;

        public RestaurantRecyclerAdapterViewHolder (View itemView){
            super(itemView);
            resName= itemView.findViewById(R.id.resNameCardTextView);
            resAddress = itemView.findViewById(R.id.resaddressCardTextView);
        }
    }

    public RestaurantRecyclerAdapter (Map<String,Restaurant> rHashMap){
        // converts hashMap into arrayList of just the restaurants.
        for (String s: rHashMap.keySet()){
            Log.d("recycleConstructor:", rHashMap.get(s).toString());
            restaurantArray.add(rHashMap.get(s));
        }
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
        Restaurant currentItem = restaurantArray.get(position);
        holder.resName.setText(currentItem.getResName());
        holder.resAddress.setText(currentItem.getResAddress());
    }

    @Override
    public int getItemCount() {
        return restaurantArray.size();
    }




}