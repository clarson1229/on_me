package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AddFriend extends Activity {
    private String userId;
    private String userJString;
    private ArrayList<User> userArrayList;
    private RecyclerView userRecyclerView;
    private FriendRecyclerAdapter uAdapter;
    private RecyclerView.LayoutManager uLayoutManager;

    private static final String TAG = "AddFriend";
    private Button saveButton;
    private User selectedUser;
    private EditText searchEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.75));

        // getting the data from main activity
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            userJString =(String) b.get("ARRAY_STRING");
            userId = (String) b.get("USER_NAME");
        }
        // converting data from a string to an object
        User[] tempList = new Gson().fromJson(userJString, User[].class);
        userArrayList = new ArrayList<User>();
        for (int i = 0; i < tempList.length; i ++){
            if (!(tempList[i].getUserId().equals(userId))){
                userArrayList.add(tempList[i]);

            }
        }

        Log.d(TAG, userArrayList.toString());
        buildRecyclerView();
        searchEditText = findViewById(R.id.input_search_friend_modal);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                uAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        saveButton = findViewById(R.id.add_friend_modal_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hideKeyboard();
                // todo get the variables from the object
                AddFriend.createFriends CF = new createFriends();
                CF.execute(userId, selectedUser.getUserId());
            }
        });
    }
    public void buildRecyclerView(){
        // todo set up recycler.
        userRecyclerView = findViewById(R.id.friend_recycleView);
        userRecyclerView.setHasFixedSize(true);
        uLayoutManager = new LinearLayoutManager(this);
        uAdapter = new FriendRecyclerAdapter(userArrayList);

        userRecyclerView.setLayoutManager(uLayoutManager);
        userRecyclerView.setAdapter(uAdapter);
        uAdapter.setOnItemClickListener(new FriendRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "itemClicked: "+userArrayList.get(position).getFullName());
                selectedUser = userArrayList.get(position);
                searchEditText.setText(userArrayList.get(position).getFullName());
            }
        });
    }
    private class createFriends extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String userId = params[0];
            String friend2 = params[1];

            String connstr = "http://connorlarson.ddns.net/restapi/create/friends.php";

            try {
                URL url = new URL(connstr);
                String param = "user1=" + userId + "&user2=" + friend2;
                Log.d(TAG, "param:" + param);

                // Open a connection using HttpURLConnection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setReadTimeout(7000);
                con.setConnectTimeout(7000);
                con.setDoOutput(true);
                con.setDoInput(true);
//                con.setInstanceFollowRedirects(false);
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
            Log.d(TAG, "onPostExecute: Result =" + result);
            // ends the activity
            Intent output = new Intent();
            // todo fix this     this result string will be different
            if (result.equals("Success 1 of 2. Success 2 of 2.")){
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
            Log.d(TAG, "Keyboard not open ");
        }
    }
}
