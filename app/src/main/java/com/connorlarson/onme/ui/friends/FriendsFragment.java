package com.connorlarson.onme.ui.friends;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.connorlarson.onme.AddFriend;
import com.connorlarson.onme.FriendAdapter;
import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.R;
import com.connorlarson.onme.User;
import com.google.gson.Gson;

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

public class FriendsFragment extends Fragment {

    private FriendsViewModel friendsViewModel;
    private static final String TAG = "Friends Page";
    private View mView;
    private ListView friendsListView;
    // data vars
    private MainActivity activity;
    private String userId;
    private String userJString;
    private ArrayList<String> friendsArray =  new ArrayList<>();
    private ArrayList<User> userArrayList= new ArrayList<>();
    private ArrayList<User> filteredArrayList= new ArrayList<>();
    private Button addFriendButton;
    private static final int MODAL_REQUEST_CODE = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel =
                ViewModelProviders.of(this).get(FriendsViewModel.class);
        mView = inflater.inflate(R.layout.fragment_friends, container, false);
        final TextView textView = mView.findViewById(R.id.text_friends);
        friendsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        friendsListView = mView.findViewById(R.id.friendsList_listView);

        activity = (MainActivity) getActivity();
        userId = activity.getUserID();
        userArrayList = activity.getUserArrayList();
        updateScrollViews();


        addFriendButton = mView.findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AddFriend.class);
                i.putExtra("USER_NAME", userId);
                i.putExtra("ARRAY_STRING",  userJString);
                startActivityForResult(i,MODAL_REQUEST_CODE);
            }
        });
        return mView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: Modal_request_code=" + requestCode+ "resultCode="+ resultCode);
        updateScrollViews();
        if (requestCode == MODAL_REQUEST_CODE) {
            if (resultCode == activity.RESULT_OK) {
                updateScrollViews();
            }
        }
    }
    private void updateScrollViews() {
        Log.d(TAG, " Upating Scrollviews");
        FriendsFragment.getFriends GFB = new getFriends();
        GFB.execute(userId);

    }
    private void setAdapter(ArrayList<String> arrayList){
        FriendAdapter favBarAdapter = new FriendAdapter(this, arrayList, userId);
        friendsListView.setAdapter(favBarAdapter);
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
            String temp = "No Friends found";
            Log.d(TAG, "value =" + !result.equals(temp));

            if (!result.equals("No Friends found")){
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
                    // filtering user array done here.
                    ArrayList<User> tempUserList = new ArrayList<>();
                    // this removes users who are already your friend when searching in modal
                    if (friendsArray.size() > 0 && userArrayList.size() > 0){
                        Log.d(TAG, "Filtering friends");
                        Log.d(TAG, "Friends added="+ friendsArray.toString());
                        Log.d(TAG, "Users Array="+ userArrayList.toString());
                        for (User user : userArrayList){
                            Boolean addedAlreadyFlag = false;
                            for (String friend : friendsArray){
                                Log.d(TAG, "user = " + user.getUserId() + " friend=" + friend);
                                Log.d(TAG, "value = " + !user.getUserId().equals(friend));
                                if (user.getUserId().equals(friend)){
                                    addedAlreadyFlag = true;
                                }
                            }
                            if (!addedAlreadyFlag){
                                tempUserList.add(user);
                            }
                        }

                        filteredArrayList.clear();
                        filteredArrayList.addAll(tempUserList);
                    }

                    Log.d(TAG, "onPostExecute: setting adapter");
                    setAdapter(friendsArray);

                    userJString = new Gson().toJson(filteredArrayList);
                    Log.d(TAG,"init:  converting array to json"+ userJString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                userJString = new Gson().toJson(userArrayList);
                Log.d(TAG,"init:  converting array to json"+ userJString);
            }
        }
    }
}