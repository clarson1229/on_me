package com.connorlarson.onme.ui.profile;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.connorlarson.onme.R;
import com.connorlarson.onme.deleteFavDrinkModal;

import java.util.ArrayList;

public class FavDrinkAdapter2 extends BaseAdapter {
    private Fragment context;
    private ArrayList<FavDrink> favDrinkArrayList;
    private static final String TAG = "Fav_Drink_Adapter";
    public FavDrinkAdapter2(Fragment c, ArrayList<FavDrink> drinkArrayList){
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        Log.d("FavDrinkAdapter ","getView: " );
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.fav_drink_row2, parent, false);
        }
        final FavDrink currentItem = (FavDrink) getItem(position);
        Log.d("FavDrinkAdapter ","getView: position" + position + " currentItem=" + currentItem.getfDrinkName());

        TextView name = convertView.findViewById(R.id.drinkNameTextView);
        TextView description = convertView.findViewById(R.id.drinkDescTextView);
        name.setText(currentItem.getfDrinkName());
        description.setText(currentItem.getfDrinkDescription());
        return convertView;
    }
}
