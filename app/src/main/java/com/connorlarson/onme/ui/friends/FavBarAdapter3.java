package com.connorlarson.onme.ui.friends;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.connorlarson.onme.R;
import com.connorlarson.onme.ui.profile.FavBar;

import java.util.ArrayList;

public class FavBarAdapter3 extends BaseAdapter {
    private Context context;
    private ArrayList<FavBar> favBarArrayList;
    private static final String TAG = "Fav_Bar_Adapter";
    public FavBarAdapter3(Context c, ArrayList<FavBar> barArrayList){
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        Log.d("FavBarAdapter ","getView: " );
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.fav_bar_row2, parent, false);
        }
        final FavBar currentItem = (FavBar) getItem(position);
        Log.d("FavBarAdapter ","getView: position" + position + " currentItem=" + currentItem.getfBarName());

        TextView name = convertView.findViewById(R.id.nameBarTextView);
        TextView address = convertView.findViewById(R.id.addressBarTextView);
        name.setText(currentItem.getfBarName());
        address.setText(currentItem.getfBarAddress());


        return convertView;
    }
}
