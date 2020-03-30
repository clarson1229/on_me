package com.connorlarson.onme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.connorlarson.onme.ui.profile.FavBar;

import java.util.ArrayList;

public class FavBarAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FavBar> favBarArrayList;

    public FavBarAdapter(Context c, ArrayList<FavBar> barArrayList){
        context = c;
        favBarArrayList = barArrayList;
    }

    @Override
    public int getCount() {
        return favBarArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return favBarArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("FavBarAdapter ","getView: " );
        if(convertView == null){
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.fav_bar_row, parent, false);
        }

        FavBar currentItem = (FavBar) getItem(position);
        Log.d("FavBarAdapter ","getView: position" + position + " currentItem=" + currentItem.getfBarName());

        TextView name = convertView.findViewById(R.id.nameBarTextView);
        TextView address = convertView.findViewById(R.id.addressBarTextView);

        name.setText(currentItem.getfBarName());
        address.setText(currentItem.getfBarAddress());


        return convertView;
    }
}
