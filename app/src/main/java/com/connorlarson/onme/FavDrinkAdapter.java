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

public class FavDrinkAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FavDrink> favDrinkArrayList;

    public FavDrinkAdapter(Context c, ArrayList<FavDrink> drinkArrayList){
        context = c;
        favDrinkArrayList = drinkArrayList;
    }

    @Override
    public int getCount() {
        return favDrinkArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return favDrinkArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("FavDrinkAdapter ","getView: " );
        if(convertView == null){
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.fav_drink_row, parent, false);
        }
        FavDrink currentItem = (FavDrink) getItem(position);
        Log.d("FavDrinkAdapter ","getView: position" + position + " currentItem=" + currentItem.getfDrinkName());

        TextView name = convertView.findViewById(R.id.drinkNameTextView);
        TextView description = convertView.findViewById(R.id.drinkDescTextView);

        name.setText(currentItem.getfDrinkName());
        description.setText(currentItem.getfDrinkDescription());


        return convertView;
    }
}
