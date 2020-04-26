package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddFavDrink extends Activity {
    private String userId;
    private static final String TAG = "AddFavBarModal";
    private Button saveButton;
    private EditText drinkName;
    private EditText drinkDescription;

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
        drinkName = findViewById(R.id.enterDrinkNameEditText);
        drinkDescription = findViewById(R.id.enterDrinkDescrEditText);
        saveButton = findViewById(R.id.favDrinkModal_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo add checks to make sure data feilds are filled out

                hideKeyboard();
                AddFavDrink.createFavDrink CFD = new createFavDrink();
                CFD.execute(userId, drinkName.getText().toString(), drinkDescription.getText().toString() );
            }
        });

    }

    private class createFavDrink extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String userId = params[0];
            String dDescription = params[1];
            String dName = params[2];



            String connstr = "http://connorlarson.ddns.net/restapi/create/fav_drinks.php";

            try {
                URL url = new URL(connstr);
                String param = "name=" + dName + "&description=" + dDescription + "&user=" + userId;
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
                    while ((line = in.readLine()) != null) {
                        // Append server response in string
                        sb.append(line);
                    }
                    result = sb.toString();
                    con.disconnect();
                    return result;
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute: Result = " + result);
            // ends the activity
            Intent output = new Intent();
            if (result.equals("Success.")){
                setResult(RESULT_OK, output);

            }else {
                setResult(RESULT_CANCELED, output);
            }
            finish();
        }
    }
    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.d(TAG, "Keyboard now open ");
        }
    }
}
