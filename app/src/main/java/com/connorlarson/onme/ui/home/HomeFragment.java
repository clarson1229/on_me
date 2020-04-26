package com.connorlarson.onme.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.R;
import com.connorlarson.onme.Restaurant;
import com.connorlarson.onme.SendDrinkPage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private Marker mMarker;

    private Map <String,Restaurant> restaurantMap =  new HashMap<String, Restaurant>();


    // data vars
    private MainActivity activity;
    private HomeViewModel homeViewModel;
    private String userId, selectedPlaceId;

    // widgets
    private EditText mSearchText;
    private ImageView mGps;

    private Button selectPlaceButton;
    private TextView selectedPlaceTextView;

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
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.d(TAG,"onMarkerClicked: marker clicked");
                    mMarker = marker;
                    selectedPlaceId = mMarker.getSnippet();
                    Log.d(TAG,"onMarkerClick: Title=" +
                            mMarker.getTitle()+ " Snippit="+ mMarker.getSnippet() );
                    selectedPlace = mMarker.getTitle();
                    selectedPlaceTextView.setText(mMarker.getTitle());
                    return false;
                }
            });
            init();
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        activity = (MainActivity) getActivity();
        userId = activity.getUserID();
        selectedPlaceTextView =  mView.findViewById(R.id.selectedPlace);
        mSearchText =  mView.findViewById(R.id.input_search);
        mGps =  mView.findViewById(R.id.ic_gps);
        selectPlaceButton = mView.findViewById(R.id.select_place_button);

//        textView.setText(userId);
        getLocationPermission();
        return mView;
    }

    private void init(){
        Log.d(TAG, "init: initializing");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == keyEvent.KEYCODE_DPAD_CENTER){

                    // Execute our method for searching
                    String searchString = mSearchText.getText().toString();
                    LatLng tempLatLong = getLocationFromAddress(activity, searchString);
                    moveCamera(tempLatLong, DEFAULT_ZOOM);
                }
                return false;
            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Center Location");
                getDeviceLocation();
            }
        });
        selectPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo popup modal to send the drink.
                Log.d(TAG, "selectPlace button clicked. Opening sendDrink Activity");
                Intent intent = new Intent(activity, SendDrinkPage.class);
                intent.putExtra("USER_NAME", userId);
                intent.putExtra("RESTAURANT", selectedPlace);
                intent.putExtra("RESTAURANT_ID", selectedPlaceId);
                startActivity(intent);

            }
        });
        hideSoftKeyboard();
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
        hideSoftKeyboard();
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
        restaurantMap=activity.getRestaurantMap();
        //todo maybe add a variable on if results have been processed yet
        //Todo custom markers on the map that display title of resturant by defualt.
        for (String s: restaurantMap.keySet()){
            LatLng tempLatLong = getLocationFromAddress(activity, restaurantMap.get(s).getResAddress());
            restaurantMap.get(s).setResLatLong(tempLatLong);
            Log.d(TAG, "onPostExecute: resName= "+ restaurantMap.get(s).getResName());
            mMap.addMarker(new MarkerOptions().position(restaurantMap.get(s).getResLatLong()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(restaurantMap.get(s).getResName()).snippet(s));
        }
    }




    public LatLng getLocationFromAddress(Context context,String strAddress) {
        Log.d(TAG, "getLocationFromAddress: converting str: "+ strAddress);
        Geocoder coder = new Geocoder(context, Locale.getDefault());
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

    private void hideSoftKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) mView
                .getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = mView.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}