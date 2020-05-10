package com.connorlarson.onme.ui.redemption;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.connorlarson.onme.R;
import com.connorlarson.onme.Restaurant;
import com.connorlarson.onme.Transaction;

import java.util.ArrayList;


public class RedemptionRecyclerAdapter extends RecyclerView.Adapter<RedemptionRecyclerAdapter.RedemptionRecyclerAdapterViewHolder> implements Filterable {
    private ArrayList<Transaction> redemptionArray;
    private ArrayList<Transaction> redemptionArrayFull;
    private ArrayList<Transaction> filteredList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private static final String TAG = "RedemRecyclerAdapter";

    @Override
    public Filter getFilter() {
        return mFilter;
    }
    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            Log.d(TAG, "performFiltering: filter=" + constraint);
            //todo this needs work if want to use
//            if (constraint == null || constraint.length() == 0){
//                filteredList.addAll(redemptionArrayFull);
//            }else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for (Restaurant restaurant : redemptionArrayFull){
//                    if (restaurant.getResName().toLowerCase().contains(filterPattern)){
//                        filteredList.add(restaurant);
//                    }
//                }
//            }

            filteredList.addAll(redemptionArrayFull);
            Log.d(TAG, "performFiltering: filteredList=" + filteredList.toString());
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(TAG, "publishResults: result = " + results.values.toString());
            redemptionArray.clear();
            redemptionArray.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }
    public static class RedemptionRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView amount;
        public TextView resName;
        public TextView sender;

        public RedemptionRecyclerAdapterViewHolder (View itemView, final OnItemClickListener listener){
            super(itemView);

            amount= itemView.findViewById(R.id.amountText);
            resName = itemView.findViewById(R.id.resNameText);
            sender = itemView.findViewById(R.id.senderText);

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

    public RedemptionRecyclerAdapter(ArrayList<Transaction> rArray){
        redemptionArray = rArray;
        redemptionArrayFull = new ArrayList<Transaction>(rArray);
        Log.d("recycleConstructor:", redemptionArray.toString());
    }

    @NonNull
    @Override
    public RedemptionRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.redemption_item, parent, false);
        RedemptionRecyclerAdapterViewHolder RRAVH = new RedemptionRecyclerAdapterViewHolder(v,mOnItemClickListener);
        return RRAVH;
    }

    @Override
    public void onBindViewHolder(@NonNull RedemptionRecyclerAdapterViewHolder holder, int position) {
        Transaction currentItem = redemptionArray.get(position);

        holder.amount.setText(currentItem.gettAmount());
        holder.resName.setText(currentItem.getRestaurantName());
        holder.sender.setText(currentItem.gettSender());
    }

    @Override
    public int getItemCount() {
        return redemptionArray.size();
    }

}