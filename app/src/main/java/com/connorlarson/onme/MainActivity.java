package com.connorlarson.onme;

import android.os.Bundle;

import com.connorlarson.onme.SharedViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.Menu;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG = "MainActivity";
    private Map<String,Restaurant> restaurantMap =  new HashMap<>();
    public Map<String, Restaurant> getRestaurantMap() {
        return restaurantMap;
    }
//    private SharedViewModel sharedViewModel;
    private String userId;
    private Pair<Double, Double> latLong = Pair.create(39.028015127394035,-94.5739747663232);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_friends, R.id.nav_favorites,
                R.id.nav_credits, R.id.nav_history, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // collecting data from home page
        userId = getIntent().getStringExtra("USER_NAME");
//        Todo Save this for later testing if needed.
//        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        Log.d("Main on create", userId);
        getRestaurantPoints GRP = new getRestaurantPoints();
//
        GRP.execute();

    }
    // Gives fragments access to the data.
    public String getUserID(){ return userId; }
    public Double getLat(){ return latLong.first; }
    public Double getLong(){ return latLong.second; }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private class getRestaurantPoints extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";

            String connstr = "http://connorlarson.ddns.net/restapi/restaurants.php";

            try{
                URL url = new URL(connstr);

                // Open a connection using HttpURLConnection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setReadTimeout(7000);
                con.setConnectTimeout(7000);
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Send
                PrintWriter out = new PrintWriter(con.getOutputStream());
                Log.d(TAG, "out Stream" + out);

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
                    while((line = in.readLine()) != null)
                    {
                        // Append server response in string
                        sb.append(line);
                    }
                    result = sb.toString();
                    con.disconnect();
                    return result;
                }
            }catch (MalformedURLException ex) {
                ex.printStackTrace();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            Log.d(TAG, "onPostExecute: Result = "+ result);
            processResults(result);
            // itrerateing through the hashMap that has now been created



        }
    }

    private void processResults (String response){
        try {
            JSONObject reader = new JSONObject(response);
            JSONArray restaurantsArray = reader.getJSONArray("Restaurants");
            restaurantMap.clear();
            for (int i = 0; i< restaurantsArray.length(); i++){
                JSONObject obj = restaurantsArray.getJSONObject(i);
                String tempId = obj.getString("RestaurantId");
                String tempName = obj.getString("RestaurantName");
                String tempAddress = obj.getString("RestaurantAddress");
                String tempPhone = obj.getString("RestaurantPhone");
                String tempHours = obj.getString("RestaurantHours");
                Log.d(TAG, "processResults: id="+ tempId +" name=" + tempName + " address=" + tempAddress +" Phone=" +tempPhone + " hours=" + tempHours);
                LatLng tempLatLong = getLocationFromAddress(getApplicationContext(), tempAddress);
                Restaurant restaurant = new Restaurant(tempName,tempAddress, tempId,tempPhone,tempHours,tempLatLong);
//                Restaurant restaurant = new Restaurant();
//                restaurant.setResId(tempId);
//                restaurant.setResName(tempName);
//                restaurant.setResAddress(tempAddress);
//                restaurant.setResHours(tempHours);
//                restaurant.setResPhone(tempPhone);
//                restaurant.setResLatLong(tempLatLong);

                restaurantMap.put(tempId,restaurant);
                Log.d("processResults: ", restaurantMap.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Log.d(TAG, "getLocationFromAddress: converting str: "+ strAddress);
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }
        Log.d(TAG, "getLocationFromAddress: latLong point= "+ p1);
        return p1;
    }



}
