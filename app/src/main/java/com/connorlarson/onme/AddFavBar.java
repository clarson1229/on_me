package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class AddFavBar extends Activity {
    private String userId;
    private Map<String,Restaurant> restaurantMap =  new HashMap<String, Restaurant>();
    private RecyclerView resRecyclerView;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav_bar);
        // Setting the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        // getting the data from main activity
        restaurantMap = (Map<String,Restaurant>) getIntent().getSerializableExtra("HASP_MAP");
        userId = getIntent().getStringExtra("USER_NAME");
        Log.d("AddFavBar", restaurantMap.toString());
        // Setting up Recycler
        resRecyclerView = findViewById(R.id.restaurantRecycleView);
        resRecyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this);
        rAdapter = new RestaurantRecyclerAdapter(restaurantMap);

        resRecyclerView.setLayoutManager(rLayoutManager);
        resRecyclerView.setAdapter(rAdapter);

    }
}