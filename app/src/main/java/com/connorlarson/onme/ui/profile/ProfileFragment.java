package com.connorlarson.onme.ui.profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.connorlarson.onme.FavBarAdapter;
import com.connorlarson.onme.FavDrinkAdapter;
import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.R;
import com.connorlarson.onme.UpdateUserProfile;

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


public class ProfileFragment extends Fragment {
    private static final String TAG = "Profile Page";
    private Button updateProfileButton;
    // textFields
    private TextView profileNameFirst;
    private TextView profileNameLast;
    private TextView profileEmail;
    private TextView profileAddress;
    // ListViews
    private ListView favBarsListView;
    private ListView favDrinksListView;

    // data vars
    private MainActivity activity;
    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private ArrayList<FavBar> favBarArray =  new ArrayList<FavBar>();
    private ArrayList<FavDrink> favDrinkArray =  new ArrayList<FavDrink>();
    private View mView;

    private static final int MODAL_REQUEST_CODE = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        ProfileViewModal =
//                ViewModelProviders.of(this).get(ProfileViewModal.class);
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        profileNameFirst = mView.findViewById(R.id.text_profile_name_first);
        profileNameLast = mView.findViewById(R.id.text_profile_name_last);
        profileEmail = mView.findViewById(R.id.text_profile_email);
        profileAddress = mView.findViewById(R.id.text_profile_address);
        favBarsListView = mView.findViewById(R.id.bars_listView);
        favDrinksListView = mView.findViewById(R.id.drinks_listView);


        activity = (MainActivity) getActivity();
        userId = activity.getUserID();
        updateUserInfo();

        updateScrollViews();
        updateProfileButton = mView.findViewById(R.id.edit_profile_button);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, UpdateUserProfile.class);
                i.putExtra("USER_NAME", userId);
                i.putExtra("FIRST_NAME", profileNameFirst.getText().toString());
                i.putExtra("LAST_NAME", profileNameLast.getText().toString());
                i.putExtra("EMAIL", profileEmail.getText().toString());
                i.putExtra("ADDRESS", profileAddress.getText().toString());

                startActivityForResult(i,MODAL_REQUEST_CODE);
            }
        });
        return mView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: Modal_request_code=" + requestCode+ "resultCode="+ resultCode);
        if (requestCode == MODAL_REQUEST_CODE) {
            if (resultCode == activity.RESULT_OK) {
                updateUserInfo();
            }
        }
    }
    private void updateUserInfo(){
        ProfileFragment.getUserInfo GUI = new getUserInfo();
        GUI.execute(userId);
    }
    private void updateScrollViews() {
        ProfileFragment.getFavBars GFB = new getFavBars();
        GFB.execute(userId);

        ProfileFragment.getFavDrinks GFD = new getFavDrinks();
        GFD.execute(userId);
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
                address = userInfo.getString("address");
                email = userInfo.getString("email");
                Log.d(TAG, "onPostExecute: userName="+ userId
                        +" first="   + firstName   + " last="  + lastName
                        +" address=" + address + " email=" + email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            profileNameFirst.setText(firstName);
            profileNameLast.setText(lastName);
            profileEmail.setText(email);
            profileAddress.setText(address);

        }
    }
    private void setBarAdapter(ArrayList<FavBar> arrayList){
        FavBarAdapter2 favBarAdapter = new FavBarAdapter2(this, arrayList);
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
        FavDrinkAdapter2 favDrinkAdapter = new FavDrinkAdapter2(this, arrayList);
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