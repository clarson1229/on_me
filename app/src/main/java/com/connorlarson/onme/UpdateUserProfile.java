package com.connorlarson.onme;

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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateUserProfile extends Activity {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String userId;
    private EditText eFirstName;
    private EditText eLastName;
    private EditText eEmail;
    private EditText eAddress;
    private Button updateButton;
    private static final String TAG = "UpdateUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
        // Setting the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.75));

        // getting the data from main activity
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            firstName =(String) b.get("FIRST_NAME");
            lastName =(String) b.get("LAST_NAME");
            email =(String) b.get("EMAIL");
            address =(String) b.get("ADDRESS");
            userId = (String) b.get("USER_NAME");
        }
        eFirstName= findViewById(R.id.firstName_editText);
        eLastName= findViewById(R.id.lastName_editText);
        eEmail= findViewById(R.id.email_editText);
        eAddress= findViewById(R.id.address_editText);
        updateButton= findViewById(R.id.userUpdateButton);
        eFirstName.setText(firstName);
        eLastName.setText(lastName);
        eEmail.setText(email);
        eAddress.setText(address);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserProfile.updateUser UU = new updateUser();
                UU.execute(userId, eFirstName.getText().toString(),eLastName.getText().toString(),eEmail.getText().toString(),eAddress.getText().toString());
            }
        });
    }
    private class updateUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String userId = params[0];
            String firstName = params[1];
            String lastName = params[2];
            String email = params[3];
            String address = params[4];

            String connstr = "http://connorlarson.ddns.net/restapi/update/user.php";

            try {
                URL url = new URL(connstr);
                String param = "user=" + userId + "&firstName=" + firstName + "&lastName=" + lastName + "&email=" + email +"&address="+address;
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
                    Log.d(TAG, "update POST request send successful: " + in);

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
            Log.d(TAG, "update onPostExecute: Result = " + result);
            // ends the activity
            Intent output = new Intent();
            if (result.equals("Success.")){
                setResult(RESULT_OK, output);
                Toast.makeText(getApplicationContext(),"Profile Successfully Updated",Toast.LENGTH_SHORT).show();

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
