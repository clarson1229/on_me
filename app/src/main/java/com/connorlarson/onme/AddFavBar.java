package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private Restaurant selectedRestaurant;
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
        // converting data back to object list then to an array list.
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
                rAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        saveButton = findViewById(R.id.add_bar_modal_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                // todo get the variables from the object
                AddFavBar.createFavBar CFB = new createFavBar();
                CFB.execute(userId,selectedRestaurant.getResId(), selectedRestaurant.getResAddress(), selectedRestaurant.getResName());
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
                selectedRestaurant = restaurantArrayList.get(position);
                searchEditText.setText(restaurantArrayList.get(position).getResName());
            }
        });
    }

    private class createFavBar extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String userId = params[0];
            String resId = params[1];
            String resAddress = params[2];
            String resName = params[3];



            String connstr = "http://connorlarson.ddns.net/restapi/create/fav_bars.php";

            try {
                URL url = new URL(connstr);
                String param = "name=" + resName + "&address=" + resAddress + "&user=" + userId + "&resId=" + resId;
                Log.d(TAG, "param:" + param);

                // Open a connection using HttpURLConnection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setReadTimeout(7000);
                con.setConnectTimeout(7000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");
                con.setFixedLengthStreamingMode(param.getBytes().length);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Send
                PrintWriter out = new PrintWriter(con.getOutputStream());
                Log.d(TAG, "out Stream" + out);
                out.print(param);
                out.close();

                con.connect();
                BufferedReader in = null;
                if (con.getResponseCode() != 200) {
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    Log.d(TAG, "!=200: " + in);

                } else {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.d(TAG, "POST request send successful: " + in);

                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        // Append server response in string
                        sb.append(line);
                    }
                    result = sb.toString();
                    con.disconnect();
                    return result;
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute: Result = " + result);
            // ends the activity
            Intent output = new Intent();
            if (result.equals("Success.")){
                setResult(RESULT_OK, output);

            }else {
                setResult(RESULT_CANCELED, output);
            }
            finish();
        }
    }
    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.d(TAG, "Keyboard now open ");
        }
    }
}