package com.connorlarson.onme;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.RestaurantRecyclerAdapterViewHolder> implements Filterable {
    private ArrayList<Restaurant> restaurantArray;
    private ArrayList<Restaurant> restaurantArrayFull;
    private ArrayList<Restaurant> filteredList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private static final String TAG = "RRecyclerAdapter";

    @Override
    public Filter getFilter() {
        return mFilter;
    }
    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            Log.d(TAG, "performFiltering: filter=" + constraint);
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(restaurantArrayFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Restaurant restaurant : restaurantArrayFull){
                    if (restaurant.getResName().toLowerCase().contains(filterPattern)){
                        filteredList.add(restaurant);
                    }
                }
            }
            Log.d(TAG, "performFiltering: filteredList=" + filteredList.toString());
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(TAG, "publishResults: result = " + results.values.toString());
            restaurantArray.clear();
            restaurantArray.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }
    public static class RestaurantRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView resName;
        public TextView resAddress;

        public RestaurantRecyclerAdapterViewHolder (View itemView, final OnItemClickListener listener){
            super(itemView);
            resName= itemView.findViewById(R.id.resNameCardTextView);
            resAddress = itemView.findViewById(R.id.resaddressCardTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RestaurantRecyclerAdapter (ArrayList<Restaurant> rArray){
        restaurantArray = rArray;
        restaurantArrayFull = new ArrayList<Restaurant>(rArray);
        Log.d("recycleConstructor:", restaurantArray.toString());
    }

    @NonNull
    @Override
    public RestaurantRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        RestaurantRecyclerAdapterViewHolder RRAVH = new RestaurantRecyclerAdapterViewHolder(v,mOnItemClickListener);
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