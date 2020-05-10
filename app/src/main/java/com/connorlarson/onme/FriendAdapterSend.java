package com.connorlarson.onme;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FriendAdapterSend extends BaseAdapter {
    private Context context;
    private ArrayList<String> friendsArrayList;
    private static final String TAG = "Friend_Adapter";

    public FriendAdapterSend(Context c, ArrayList<String> friendList){
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        Log.d("FriendAdapter ","getView: " );
        if(convertView == null){
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.friend_row2, parent, false);
        }

        final String currentItem = (String) getItem(position);
        Log.d("FriendAdapter ","getView: position" + position + " currentItem=" + currentItem);

        TextView name = convertView.findViewById(R.id.friendTextView);
        name.setText(currentItem);

        return convertView;
    }
}
