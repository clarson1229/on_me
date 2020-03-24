package com.connorlarson.onme.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.Manifest;
import com.connorlarson.onme.R;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // map stuff
    private GoogleMap mGoogleMap;
    private View mView;

    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLong;

    private MainActivity activity;
    private HomeViewModel homeViewModel;
    private String userId;

    private String selectedPlace;

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
        initializeMap();


        return mView;
    }


    private void initializeMap() {
        if (mGoogleMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // seting user location
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // store user LatLong
                userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(userLatLong).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("your Location"));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
                LatLng bradys = new LatLng(activity.getLat(), activity.getLong());
                mGoogleMap.addMarker(new
                        MarkerOptions().position(bradys).title("Brady's").snippet("Address: 5424 Troost Ave, Kansas City, MO 64110"));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };


        // do your map stuff here
        askLocationPermission();

    }

    private void askLocationPermission() {

        Dexter.withActivity(activity).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                //TODo if not working change android.Manifest to manifest and then change manifest file
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(userLatLong).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("your Location"));
                LatLng bradys = new LatLng(activity.getLat(), activity.getLong());
                mGoogleMap.addMarker(new
                        MarkerOptions().position(bradys).title("Brady's"));


                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        selectedPlace = marker.getSnippet();
        final TextView seletectedTextView = mView.findViewById(R.id.selectedPlace);
        seletectedTextView.setText(selectedPlace);
        return false;
    }
}