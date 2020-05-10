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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class deleteFriendModal extends AppCompatActivity {
    String friend1, friend2;
    Button cancelButton, deleteButton;
    private static final String TAG = "DeleteFriend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_friend_modal);
        // Setting the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.35));

        // getting the data from main activity
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            friend1 = (String) b.get("FRIEND_1");
            friend2 = (String) b.get("FRIEND_2");
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
                deleteFriendModal.deleteFriend DF = new deleteFriend();
                DF.execute(friend1,friend2);
            }
        });

    }
    private class deleteFriend extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            String friend_1 = params[0];
            String friend_2 = params[1];

            String connstr = "http://connorlarson.ddns.net/restapi/delete/friends.php";

            try{
                URL url = new URL(connstr);
                String param = "friend1="+friend_1+"&friend2="+friend_2;
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
            if (result.equals("Success 1 of 2. Success 2 of 2.")){
                setResult(RESULT_OK, output);
                Toast.makeText(getApplicationContext(),"Friend Successfully deleted",Toast.LENGTH_SHORT).show();
            }else {
                setResult(RESULT_CANCELED, output);
            }
            finish();
        }
    }
}
