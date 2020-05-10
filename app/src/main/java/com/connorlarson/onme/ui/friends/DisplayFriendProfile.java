package com.connorlarson.onme.ui.friends;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.connorlarson.onme.R;
import com.connorlarson.onme.ui.profile.FavBar;
import com.connorlarson.onme.ui.profile.FavDrink;

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

public class DisplayFriendProfile extends Activity {
    private String userId,friend;
    private String firstName;
    private String lastName;
    private ArrayList<FavBar> favBarArray =  new ArrayList<FavBar>();
    private ArrayList<FavDrink> favDrinkArray =  new ArrayList<FavDrink>();
    private static final String TAG = "DisplayFriendProfile";
    // textFields
    private TextView profileNameFirst;
    private TextView profileNameLast;

    // ListViews
    private ListView favBarsListView;
    private ListView favDrinksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_friend_profile);
        // Setting the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9),(int)(height*.80));

        // getting the data from main activity
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            userId = (String) b.get("FRIEND_2");
            friend = (String) b.get("FRIEND_1");
        }
        profileNameFirst = findViewById(R.id.text_profile_name_first);
        profileNameLast = findViewById(R.id.text_profile_name_last);
        favBarsListView = findViewById(R.id.bars_listView);
        favDrinksListView = findViewById(R.id.drinks_listView);
        updateUserInfo();
        updateScrollViews();
    }

    private void updateUserInfo(){
        DisplayFriendProfile.getUserInfo GUI = new getUserInfo();
        GUI.execute(friend);
    }
    private void updateScrollViews() {
        DisplayFriendProfile.getFavBars GFB = new getFavBars();
        GFB.execute(friend);

        DisplayFriendProfile.getFavDrinks GFD = new getFavDrinks();
        GFD.execute(friend);
    }

    private class getUserInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            String user = params[0];

            String connstr = "http://connorlarson.ddns.net/restapi/profile.php";

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
                JSONObject userInfo = reader.getJSONObject("userInfo");

                firstName = userInfo.getString("firstName");
                lastName = userInfo.getString("lastName");

                Log.d(TAG, "onPostExecute: userName="+ userId
                        +" first="   + firstName   + " last="  + lastName
                        );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            profileNameFirst.setText(firstName);
            profileNameLast.setText(lastName);


        }
    }
    private void setBarAdapter(ArrayList<FavBar> arrayList){
        FavBarAdapter3 favBarAdapter = new FavBarAdapter3(this, arrayList);
        favBarsListView.setAdapter(favBarAdapter);
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
                setBarAdapter(favBarArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void setDrinkAdapter(ArrayList<FavDrink> arrayList){
        FavDrinkAdapter3 favDrinkAdapter = new FavDrinkAdapter3(this, arrayList);
        favDrinksListView.setAdapter(favDrinkAdapter);
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
                setDrinkAdapter(favDrinkArray);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
