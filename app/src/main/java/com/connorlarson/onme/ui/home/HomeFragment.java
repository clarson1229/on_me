package com.connorlarson.onme.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.R;
import com.connorlarson.onme.Restaurant;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    // statics
    private static final String TAG = "HomeFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_CODE = 1234;
    private static final float DEFAULT_ZOOM =15;
    // map vars
    private  Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private View mView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String selectedPlace;


    private Map <String,Restaurant> restaurantMap =  new HashMap<String, Restaurant>();


    // data vars
    private MainActivity activity;
    private HomeViewModel homeViewModel;
    private String userId;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map is Ready");
        mMap = googleMap;
        if(mLocationPermissionsGranted){
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            addRestaurantMarkers();
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = mView.findViewById(R.id.text_home);
// This is how to get data from shared modal.
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        // sets the text view text
        activity = (MainActivity) getActivity();
        userId = activity.getUserID();
        textView.setText(userId);
        getLocationPermission();

        return mView;
    }
    private void getDeviceLocation(){
        Log.d(TAG,"Getting Device location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        try{
            if (mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found Locations");
                            Location currentLocation =(Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),DEFAULT_ZOOM);
                        }else{
                            Log.d(TAG,"onComplete: current location is null");
                            Toast.makeText(activity,"Unable to get current location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.d(TAG, "getdeviceLocation: error" + e.getMessage());
        }

    }
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "Moving Camera to lat: " + latLng.latitude + ", Long: "+ latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }
    private  void initMap(){
        Log.d(TAG, "Initializing map");
        if (mMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(HomeFragment.this);
        }
    }
    private void getLocationPermission(){
        Log.d(TAG, "getting Permissions");

        Dexter.withActivity(activity).withPermission(FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionsGranted = false;
                    return;
                }else {
                    // intialize map
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
    private void addRestaurantMarkers() {
        getRestaurantPoints ALT = new getRestaurantPoints();

        ALT.execute();
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

            //todo maybe add a variable on if results have been processed yet
            for (String s: restaurantMap.keySet()){
                Log.d(TAG, "onPostExecute: resName= "+ restaurantMap.get(s).getResName());
                mMap.addMarker(new MarkerOptions().position(restaurantMap.get(s).getResLatLong()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(restaurantMap.get(s).getResName()).snippet(s));
            }
        }
    }
    private void processResults (String response){
        try {
            JSONObject reader = new JSONObject(response);
            JSONArray restaurantsArray = reader.getJSONArray("Restaurants");
            for (int i = 0; i< restaurantsArray.length(); i++){
                JSONObject obj = restaurantsArray.getJSONObject(i);
                    String tempId = obj.getString("RestaurantId");
                    String tempName = obj.getString("RestaurantName");
                    String tempAddress = obj.getString("RestaurantAddress");
                    String tempPhone = obj.getString("RestaurantPhone");
                    String tempHours = obj.getString("RestaurantHours");
                    Log.d(TAG, "processResults: id="+ tempId +" name=" + tempName + " address=" + tempAddress +" Phone=" +tempPhone + " hours=" + tempHours);
                    LatLng tempLatLong = getLocationFromAddress(activity, tempAddress);

                    Restaurant restaurant = new Restaurant(tempName,tempAddress, tempId,tempPhone,tempHours,tempLatLong);
                    restaurantMap.put(tempId,restaurant);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {
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