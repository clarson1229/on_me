package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

public class AddFavDrink extends Activity {
    private String userId;
    private static final String TAG = "AddFavBarModal";
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav_drink);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.75));

        // getting the data from main activity
        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            userId = (String) b.get("USER_NAME");
        }

        // Setting up buttons and edit text

    }
}
