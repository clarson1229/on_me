package com.connorlarson.onme;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.connorlarson.onme.ui.friends.DisplayFriendProfile;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {
    private Fragment context;
    private ArrayList<String> friendsArrayList;
    private static final String TAG = "Friend_Adapter";
    private static final int MODAL_REQUEST_CODE = 0;
    private String userId;

    public FriendAdapter(Fragment c, ArrayList<String> friendList, String user){
        context = c;
        friendsArrayList = friendList;
        userId = user;
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
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.friend_row, parent, false);
        }

        final String currentItem = (String) getItem(position);
        Log.d("FriendAdapter ","getView: position" + position + " currentItem=" + currentItem);


        // onclick for delete
        ImageView delete = convertView.findViewById(R.id.deleteIcon);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent.getContext(), deleteFriendModal.class);
                i.putExtra("FRIEND_1", currentItem);
                i.putExtra("FRIEND_2", userId);

                context.startActivityForResult(i,MODAL_REQUEST_CODE);
            }
        });
        TextView name = convertView.findViewById(R.id.friendTextView);
        name.setText(currentItem);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent.getContext(), DisplayFriendProfile.class);

                i.putExtra("FRIEND_1", currentItem);
                i.putExtra("FRIEND_2", userId);

                context.startActivityForResult(i,MODAL_REQUEST_CODE);
            }
        });

        return convertView;
    }
}
