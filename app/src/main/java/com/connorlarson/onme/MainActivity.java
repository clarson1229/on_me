package com.connorlarson.onme;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.os.AsyncTask;
import android.view.Menu;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG = "MainActivity";
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private Map<String,Restaurant> restaurantMap =  new HashMap<>();
    public Map<String, Restaurant> getRestaurantMap() {
        return restaurantMap;
    }
    private ArrayList<User> userArrayList =  new ArrayList<>();
    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }


//    private SharedViewModel sharedViewModel;
    private String userId;
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
                R.id.nav_home, R.id.nav_redemption, R.id.nav_friends, R.id.nav_favorites,
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
        GRP.execute();

        getUsers GU = new getUsers();
        GU.execute();

    }
    // Gives fragments access to the data.
    public String getUserID(){ return userId; }



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
            Log.d(TAG, "getRestaurantPoints started");
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
                Log.d(TAG, "restaurants out Stream" + out);

                out.close();

                con.connect();
                BufferedReader in = null;
                if (con.getResponseCode() != 200) {
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    Log.d(TAG, "!=200: " + in);

                } else {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.d(TAG, "restaurants POST request send successful: " + in);

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
            Log.d(TAG, "onPostExecute: restaurants Result = "+ result);
            processResults(result);
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

                Restaurant restaurant = new Restaurant(tempName,tempAddress, tempId,tempPhone,tempHours,null);
                restaurantMap.put(tempId,restaurant);
                Log.d("processResults: ", restaurantMap.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private class getUsers extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "getUsers: started");
            String result="";

            String connstr = "http://connorlarson.ddns.net/restapi/users.php";

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
                Log.d(TAG, "Users out Stream" + out);

                out.close();

                con.connect();
                BufferedReader in = null;
                if (con.getResponseCode() != 200) {
                    in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    Log.d(TAG, "!=200: " + in);

                } else {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.d(TAG, "Users POST request send successful: " + in);

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
            Log.d(TAG, "onPostExecute: users Result = "+ result);
            processResultUsers(result);
        }
    }

    private void processResultUsers (String response){
        try {
            JSONObject reader = new JSONObject(response);
            JSONArray usersArray = reader.getJSONArray("Users");
            userArrayList.clear();
            for (int i = 0; i< usersArray.length(); i++){
                JSONObject obj = usersArray.getJSONObject(i);
                String tempId = obj.getString("userId");
                String tempFirst = obj.getString("firstName");
                String tempLast = obj.getString("lastName");

                Log.d(TAG, "processResultUsers: id="+ tempId +" tempFirst=" + tempFirst + " tempLast=" + tempLast);

                User user = new User(tempId,tempFirst,tempLast);
                userArrayList.add(user);
                Log.d(TAG,"processResultUsers: " + userArrayList.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
