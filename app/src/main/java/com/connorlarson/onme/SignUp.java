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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignUp extends Activity {
    private EditText userID;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText address;
    private EditText password;
    private Button saveButton;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Setting the size of the popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.75));
        userID = findViewById(R.id.userName_editText);
        firstName = findViewById(R.id.firstName_editText);
        lastName = findViewById(R.id.lastName_editText);
        email = findViewById(R.id.userEmail_editText);
        address = findViewById(R.id.userAddress_editText);
        password = findViewById(R.id.userPassword_editText);
        saveButton = findViewById(R.id.saveSignupButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                SignUp.createUser CU = new createUser();
                CU.execute(userID.getText().toString(),firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),address.getText().toString(),password.getText().toString());
            }
        });

    }
    private class createUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String userId = params[0];
            String firstName = params[1];
            String lastName = params[2];
            String email = params[3];
            String address = params[4];
            String password = params[5];

            String connstr = "http://connorlarson.ddns.net/restapi/create/user.php";

            try {
                URL url = new URL(connstr);
                String param = "user=" + userId + "&firstName=" + firstName + "&lastName=" + lastName + "&email=" + email +"&address="+address+"&password="+password;
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
                    Log.d(TAG, "Signup POST request send successful: " + in);

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
                Toast.makeText(getApplicationContext(),"User Successfully Created",Toast.LENGTH_SHORT).show();


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
