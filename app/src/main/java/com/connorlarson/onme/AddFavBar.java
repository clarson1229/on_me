package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFavBar extends Activity {
    private String userId;
    private String restaurantString;
    private Restaurant[] restaurantArrayList;
    private RecyclerView resRecyclerView;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private static final String TAG = "AddFavBarModal";
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav_bar);
        // Setting the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.75));

        // getting the data from main activity
        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            restaurantString =(String) b.get("ARRAY_STRING");
            userId = (String) b.get("USER_NAME");
        }
        restaurantArrayList = new Gson().fromJson(restaurantString, Restaurant[].class);
        int sizeOf = restaurantArrayList.length;
//        Log.d(TAG, String.valueOf(sizeOf));
//        for (int x=0; x< sizeOf;x++){
//            Log.d(TAG,restaurantArrayList[x].toString());
//        }

        Log.d("AddFavBar", restaurantArrayList.toString());
        // Setting up Recycler
        resRecyclerView = findViewById(R.id.restaurantRecycleView);
        resRecyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this);
        rAdapter = new RestaurantRecyclerAdapter(restaurantArrayList);

        resRecyclerView.setLayoutManager(rLayoutManager);
        resRecyclerView.setAdapter(rAdapter);

    }
}