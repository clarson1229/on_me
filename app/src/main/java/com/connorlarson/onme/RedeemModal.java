package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RedeemModal extends Activity {
    private String userId,transactionId,sender,restaurantName, transactionAmount, message;
    private static final String TAG = "RedeemModal";
    private Button redeemButton;
    private TextView senderText, resNameText, amountText, messageText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_modal);
        // Setting the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9),(int)(height*.80));

        // getting the data from main activity
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            userId = (String) b.get("USER_NAME");
            transactionId = (String) b.get("TRANSACTION_ID");
            restaurantName = (String) b.get("RESTAURANT_NAME");
            sender = (String) b.get("SENDER");
            transactionAmount = (String) b.get("TRANSACTION_AMOUNT");
            message = (String) b.get("MESSAGE");
        }

        redeemButton= findViewById(R.id.redeemButton);
        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Redeem Clicked");
                RedeemModal.redeemTransaction RT = new redeemTransaction();
                RT.execute(transactionId);
            }
        });

        senderText=findViewById(R.id.sender);
        resNameText=findViewById(R.id.resName);
        messageText=findViewById(R.id.messageText);
        amountText=findViewById(R.id.amount);

        senderText.setText(sender);
        resNameText.setText(restaurantName);
        messageText.setText(message);
        amountText.setText(transactionAmount);

    }
    private class redeemTransaction extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String tranId = params[0];


            String connstr = "http://connorlarson.ddns.net/restapi/update/transaction.php";

            try {
                URL url = new URL(connstr);
                String param = "tranId=" + tranId;
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
                Toast.makeText(getApplicationContext(),"Drink Successfully Redeemed",Toast.LENGTH_SHORT).show();
            }else {
                setResult(RESULT_CANCELED, output);
            }
            finish();
        }
    }
}
