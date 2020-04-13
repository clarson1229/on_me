package com.connorlarson.onme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.connorlarson.onme.ui.profile.FavDrink;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> friendsArrayList;

    public FriendAdapter(Context c, ArrayList<String> friendList){
        context = c;
        friendsArrayList = friendList;
    }

    @Override
    public int getCount() {
        return friendsArrayList.size();
    }

    @Override
    public String getItem(int i) {
        return friendsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("FriendAdapter ","getView: " );
        if(convertView == null){
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.friend_row, parent, false);
        }

        String currentItem = (String) getItem(position);
        Log.d("FriendAdapter ","getView: position" + position + " currentItem=" + currentItem);

        TextView name = convertView.findViewById(R.id.friendTextView);

        name.setText(currentItem);


        return convertView;
    }
}
