package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class SendDrinkPage extends AppCompatActivity {

    private static final String TAG = "Send Drinks Page";
    private ListView friendsSendListView;
    // data vars
    private String userId, selectedRestaurant,selectedRestaurantId, selectedUserID;
    private ArrayList<String> friendsArray =  new ArrayList<String>();
    private EditText dAmountEdit,dMessageEdit;
    private Button sendButton, backButton;
    private TextView selectedRestaurantTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_drink_page);
        // getting extras
        userId = getIntent().getStringExtra("USER_NAME");
        selectedRestaurant = getIntent().getStringExtra("RESTAURANT");
        selectedRestaurantId = getIntent().getStringExtra("RESTAURANT_ID");
        // getting buttons, textFields and  listViews
        dAmountEdit= findViewById(R.id.drink_amount_edit_text);
        dMessageEdit= findViewById(R.id.drink_message_edit_text);
        sendButton= findViewById(R.id.send_drink_button);
        backButton= findViewById(R.id.back_button);
        friendsSendListView= findViewById(R.id.friendsList_send_listView);
        selectedRestaurantTextView= findViewById(R.id.restaurant_title_textview);
        selectedRestaurantTextView.setText(selectedRestaurant);
        updateScrollViews();
        friendsSendListView.setClickable(true);
        // listeners set up.
        friendsSendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object o = friendsSendListView.getItemAtPosition(position);
                selectedUserID =(String)o;
                Log.d(TAG, "onItemClick: selected userId = "+selectedUserID);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dAmountEditString = dAmountEdit.getText().toString();
                String dMessageEditString = dMessageEdit.getText().toString();
                Log.d(TAG, "onClick: SendButton. dAmount="+
                        dAmountEditString+" dMessage= "+
                        dMessageEditString+" sendingUSer= "+
                        userId+" recipientUSer= "+
                        selectedUserID+" recipientRestaurant= "+
                        selectedRestaurant+" recipientRestaurantID= "+
                        selectedRestaurantId);
                hideKeyboard();
                SendDrinkPage.createTransaction CT = new createTransaction();
                CT.execute(userId,selectedUserID,dAmountEditString,dMessageEditString,selectedRestaurantId);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                finish();
            }
        });

    }

    private void updateScrollViews() {
        SendDrinkPage.getFriends GFB = new getFriends();
        GFB.execute(userId);
    }
    private class getFriends extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            String user = params[0];

            String connstr = "http://connorlarson.ddns.net/restapi/friends.php";

            try{
                URL url = new URL(connstr);
                String param = "user="+user;
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
            try {
                JSONObject reader = new JSONObject(result);
                JSONArray friendArray = reader.getJSONArray("Friends");
                friendsArray.clear();
                for (int i = 0; i< friendArray.length(); i++){
                    JSONObject obj = friendArray.getJSONObject(i);
                    String tempFriend = obj.getString("Friend");

                    Log.d(TAG, "onPostExecute: friend="+ tempFriend);

                    friendsArray.add(tempFriend);
                }
                // setting the list view with the data
                Log.d(TAG, "onPostExecute: setting adapter");
                FriendAdapter favBarAdapter = new FriendAdapter(getBaseContext(), friendsArray);
                friendsSendListView.setAdapter(favBarAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class createTransaction extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            String sender = params[0];
            String reciever = params[1];
            String amount = params[2];
            String message = params[3];
            String restId = params[4];


            String connstr = "http://connorlarson.ddns.net/restapi/create/transactions.php";

            try{
                URL url = new URL(connstr);
                String param = "sender="+sender+"&reciever="+reciever+"&amount="+amount+"&message="+message+"&restId="+restId;
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
