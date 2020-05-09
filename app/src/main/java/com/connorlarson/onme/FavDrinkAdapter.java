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
import com.connorlarson.onme.ui.profile.FavDrink;
import java.util.ArrayList;

public class FavDrinkAdapter extends BaseAdapter {
    private Fragment context;
    private ArrayList<FavDrink> favDrinkArrayList;
    private static final String TAG = "Fav_Drink_Adapter";
    private static final int MODAL_REQUEST_CODE = 0;
    public FavDrinkAdapter(Fragment c, ArrayList<FavDrink> drinkArrayList){
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
                    inflate(R.layout.fav_drink_row, parent, false);
        }
        final FavDrink currentItem = (FavDrink) getItem(position);
        Log.d("FavDrinkAdapter ","getView: position" + position + " currentItem=" + currentItem.getfDrinkName());

        TextView name = convertView.findViewById(R.id.drinkNameTextView);
        TextView description = convertView.findViewById(R.id.drinkDescTextView);
        // onclick for delete
        ImageView delete = convertView.findViewById(R.id.deleteIcon);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo popup modal here
                Intent i = new Intent(parent.getContext(), deleteFavDrinkModal.class);
                i.putExtra("DRINK_ID", currentItem.getfDrinkId());
                context.startActivityForResult(i,MODAL_REQUEST_CODE);
            }
        });

        name.setText(currentItem.getfDrinkName());
        description.setText(currentItem.getfDrinkDescription());
        return convertView;
    }
}
