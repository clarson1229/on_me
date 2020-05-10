package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.connorlarson.onme.ui.favorites.FavoritesFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class deleteFavDrinkModal extends AppCompatActivity {
    String drinkId;
    Button cancelButton, deleteButton;
    private static final String TAG = "DeleteFavDrink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_fav_drink_modal);
        // Setting the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.35));

        // getting the data from main activity
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            drinkId = (String) b.get("DRINK_ID");
        }
        cancelButton= findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deleteButton= findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavDrinkModal.deleteFavDrinks DFD = new deleteFavDrinks();
                DFD.execute(drinkId);
            }
        });

    }
    private class deleteFavDrinks extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            String drinkId = params[0];

            String connstr = "http://connorlarson.ddns.net/restapi/delete/fav_drinks.php";

            try{
                URL url = new URL(connstr);
                String param = "drinkId="+drinkId;
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
            // ends the activity
            Intent output = new Intent();
            if (result.equals("Success.")){
                setResult(RESULT_OK, output);
                Toast.makeText(getApplicationContext(),"Favorite Drink Successfully deleted",Toast.LENGTH_SHORT).show();
            }else {
                setResult(RESULT_CANCELED, output);
            }

            finish();
        }
    }
}
