package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFavBar extends Activity {
    private String userId;
    private String restaurantString;

    private ArrayList<Restaurant> restaurantArrayList;
    private RecyclerView resRecyclerView;
    private RestaurantRecyclerAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private static final String TAG = "AddFavBarModal";
    private Button saveButton;
    private String selectedRestaurantName;
    private String selectedRestaurantId;
    private int selectedPosition;
    private EditText searchEditText;


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
        if(b!=null) {
            restaurantString =(String) b.get("ARRAY_STRING");
            userId = (String) b.get("USER_NAME");
        }
        // converting data back to object list.
        Restaurant[] tempList = new Gson().fromJson(restaurantString, Restaurant[].class);
        restaurantArrayList = new ArrayList<Restaurant>();

        for (int i = 0; i < tempList.length; i ++){
            restaurantArrayList.add(tempList[i]);
        }




        Log.d("AddFavBar", restaurantArrayList.toString());
        // Setting up Recycler
        buildRecyclerView();
        searchEditText = findViewById(R.id.input_search_favbar_modal);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d(TAG, "onTextChanged: new text "+ s);
//                rAdapter.getFilter().filter(s);
//                searchEditText.setText(s.toString());
                rAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void buildRecyclerView(){
        resRecyclerView = findViewById(R.id.restaurantRecycleView);
        resRecyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this);
        rAdapter = new RestaurantRecyclerAdapter(restaurantArrayList);

        resRecyclerView.setLayoutManager(rLayoutManager);
        resRecyclerView.setAdapter(rAdapter);
        rAdapter.setOnItemClickListener(new RestaurantRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "itemClicked: "+restaurantArrayList.get(position).getResName());
                selectedRestaurantName = restaurantArrayList.get(position).getResName();
                selectedRestaurantId = restaurantArrayList.get(position).getResId();
                selectedPosition = position;
                searchEditText.setText(restaurantArrayList.get(position).getResName());
            }
        });
    }
}