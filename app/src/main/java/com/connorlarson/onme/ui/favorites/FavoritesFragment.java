package com.connorlarson.onme.ui.favorites;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.connorlarson.onme.AddFavBar;
import com.connorlarson.onme.AddFavDrink;
import com.connorlarson.onme.FavBarAdapter;
import com.connorlarson.onme.FavDrinkAdapter;
import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.R;
import com.connorlarson.onme.Restaurant;
import com.connorlarson.onme.ui.profile.FavBar;
import com.connorlarson.onme.ui.profile.FavDrink;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

public class FavoritesFragment extends Fragment {

    private static final String TAG = "Favorites Page";
    private View mView;
    private ListView favBarsListView;
    private ListView favDrinksListView;
    // data vars
    private MainActivity activity;
    private String userId;
    private ArrayList<FavBar> favBarArray =  new ArrayList<FavBar>();
    private ArrayList<FavDrink> favDrinkArray =  new ArrayList<FavDrink>();
    private Button addBarButton;
    private Button addDrinkButton;
    private Map<String, Restaurant> restaurantMap =  new HashMap<>();
    private ArrayList<Restaurant> restaurantArray = new ArrayList<>();
    private String restaurantJString;

    private static final int MODAL_REQUEST_CODE = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favorites, container, false);
        favBarsListView = mView.findViewById(R.id.favorite_bar_ListView);
        favDrinksListView = mView.findViewById(R.id.favorite_drink_ListView);


        addBarButton = mView.findViewById(R.id.add_favorite_bar_button);
        addDrinkButton = mView.findViewById(R.id.add_favorite_drink_button);

        activity = (MainActivity) getActivity();
        userId = activity.getUserID();
        init();
        return mView;
    }
    private void init() {
        updateScrollViews();
        restaurantMap = activity.getRestaurantMap();
        for (String s: restaurantMap.keySet()){
            restaurantArray.add(restaurantMap.get(s));
        }
        restaurantJString = new Gson().toJson(restaurantArray);
        Log.d(TAG,"init:  converting array to json"+ restaurantJString);

        addBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AddFavBar.class);
                i.putExtra("USER_NAME", userId);
                i.putExtra("ARRAY_STRING",  restaurantJString);
//                startActivity(i);
                startActivityForResult(i,MODAL_REQUEST_CODE);
            }
        });
        addDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AddFavDrink.class);
                i.putExtra("USER_NAME", userId);
//                startActivity(i);
                startActivityForResult(i,MODAL_REQUEST_CODE);


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onactivityResult: Modal_request_code=" + requestCode+ "resultCode="+ resultCode);
        if (requestCode == MODAL_REQUEST_CODE) {
            if (resultCode == activity.RESULT_OK) {
                updateScrollViews();
            }
        }
    }

    private void updateScrollViews() {
        FavoritesFragment.getFavBars GFB = new getFavBars();
        GFB.execute(userId);

        FavoritesFragment.getFavDrinks GFD = new getFavDrinks();
        GFD.execute(userId);
    }

    private class getFavBars extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            String user = params[0];

            String connstr = "http://connorlarson.ddns.net/restapi/fav_bars.php";

            try{
                URL url = new URL(connstr);
                String param = "user="+user;
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
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray favBarsArray = reader.getJSONArray("FavBars");
                favBarArray.clear();
                for (int i = 0; i< favBarsArray.length(); i++){
                    JSONObject obj = favBarsArray.getJSONObject(i);
                    String tempFavId = obj.getString("FavBarId");
                    String tempName = obj.getString("BarName");
                    String tempAddress = obj.getString("BarAddress");
                    String tempResId = obj.getString("RestaurantId");
                    Log.d(TAG, "onPostExecute: id="+ tempFavId +" name=" + tempName + " address=" + tempAddress +" ResId=" +tempResId);

                    FavBar favBar = new FavBar(tempName,tempAddress,tempFavId,tempResId);
                    favBarArray.add(favBar);
                }

                Log.d(TAG, "onPostExecute: setting adapter");
                FavBarAdapter favBarAdapter = new FavBarAdapter(activity, favBarArray);
                favBarsListView.setAdapter(favBarAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class getFavDrinks extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            String user = params[0];

            String connstr = "http://connorlarson.ddns.net/restapi/fav_drinks.php";

            try{
                URL url = new URL(connstr);
                String param = "user="+user;
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
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray favBarsArray = reader.getJSONArray("FavDrinks");
                favDrinkArray.clear();
                for (int i = 0; i< favBarsArray.length(); i++){
                    JSONObject obj = favBarsArray.getJSONObject(i);
                    String tempDrinkId = obj.getString("DrinkId");
                    String tempName = obj.getString("DrinkName");
                    String tempDescription = obj.getString("DrinkDescription");
                    Log.d(TAG, "onPostExecute: id="+ tempDrinkId +" name=" + tempName + " description=" + tempDescription);

                    FavDrink favDrink = new FavDrink(tempDrinkId,tempName,tempDescription);
                    favDrinkArray.add(favDrink);
                }
                Log.d(TAG, "onPostExecute: setting adapter");
                    FavDrinkAdapter favDrinkAdapter = new FavDrinkAdapter(activity, favDrinkArray);
                favDrinksListView.setAdapter(favDrinkAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}