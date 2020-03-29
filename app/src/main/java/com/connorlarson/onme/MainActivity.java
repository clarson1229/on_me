package com.connorlarson.onme;

import android.os.Bundle;

import com.connorlarson.onme.SharedViewModel;
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

import android.view.Menu;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    private SharedViewModel sharedViewModel;

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
        userId = getIntent().getStringExtra("USER_NAME");
//        Todo Save this for later testing if needed.
//        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        Log.d("Main on create", userId);
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

    public void addFriend(View view) {
//        todo load popup activicty to allow user to search for friend.
        // this will hit the database and will need async call
    }

    public void editProfileinfo(View view) {
//        TOdo add pop activity to edit profile information

    }

    public void addFavBar(View view) {
        // Todo add pop up activity to allow user to add a fav bar
    }


    public void addFavDrink(View view) {
        // todo add popup activity to allow user to add drink
    }

    public void addFavoriteBar(View view) {
        // Todo add pop up activity to allow user to add a fav bar For favorites Tab
    }

    public void addFavoriteDrink(View view) {
        // Todo add pop up activity to allow user to add a fav drink for Favorites Tab

    }

    public void add5ToAccount(View view) {
//        todo add activity to allow user to add 5$ to account
    }

    public void add10ToAccount(View view) {
        //        todo add activity to allow user to add 10$ to account
    }

    public void add20ToAccount(View view) {
        //        todo add activity to allow user to add 20$ to account
    }


    public void Load_points(View view) {
        // Todo this button adds points to map.

    }
}
