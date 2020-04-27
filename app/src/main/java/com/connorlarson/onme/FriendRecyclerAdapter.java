package com.connorlarson.onme;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.FriendRecyclerAdapterViewHolder> implements Filterable {
    private ArrayList<User> userArray;
    private ArrayList<User> userArrayFull;
    private ArrayList<User> filteredList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private static final String TAG = "FRecyclerAdapter";


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
                filteredList.addAll(userArrayFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (User user : userArrayFull){
                    // todo this is where the filtering will need to change
                    String fullName = user.getFirstName() + " "+ user.getLastName();
                    if (fullName.toLowerCase().contains(filterPattern)){
                        filteredList.add(user);
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
            userArray.clear();
            userArray.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }
    public static class FriendRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView firstName;
        public TextView lastName;
        public FriendRecyclerAdapterViewHolder (View itemView, final OnItemClickListener listener){
            super(itemView);
            firstName = itemView.findViewById(R.id.firstNameCardTextView);
            lastName = itemView.findViewById(R.id.lastNameCardTextView);
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

    public FriendRecyclerAdapter (ArrayList<User> rArray){
        userArray = rArray;
        userArrayFull = new ArrayList<User>(rArray);
        Log.d("recycleConstructor:", userArray.toString());
    }
    @NonNull
    @Override
    public FriendRecyclerAdapter.FriendRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        FriendRecyclerAdapterViewHolder FRAVH = new FriendRecyclerAdapterViewHolder(v,mOnItemClickListener);
        return FRAVH;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRecyclerAdapter.FriendRecyclerAdapterViewHolder holder, int position) {
        User currentItem = userArray.get(position);
        holder.firstName.setText(currentItem.getFirstName());
        holder.lastName.setText(currentItem.getLastName());
    }

    @Override
    public int getItemCount() {
        return userArray.size();
    }
}
