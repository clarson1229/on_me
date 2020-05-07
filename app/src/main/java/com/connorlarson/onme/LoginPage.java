package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginPage extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView results;
    private Button signUp;
    private static final String TAG = "Login Pageee";
    private static final int MODAL_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        username = (EditText)findViewById(R.id.username_text);
        password = (EditText)findViewById(R.id.password_text);
        results = (TextView)findViewById(R.id.Results_text);
        signUp = findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SignUp.class);

//                startActivity(i);
                startActivityForResult(i,MODAL_REQUEST_CODE);

            }
        });
    }

    public void attemptLogIn(View view) {
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        attemptLoginTask ALT = new attemptLoginTask();

        ALT.execute(usernameString, passwordString);
    }
    private void processResults (String response){

        Log.d(TAG,  response);
        try{
            JSONObject reader = new JSONObject(response);
            JSONObject response1  = reader.getJSONObject("response");
            String userName = response1.getString("userName");
            String status = response1.getString("status");
            Log.d(TAG, userName);
            if (status == "true"){
                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_NAME", userName);

                startActivity(intent);
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"UserName or Password incorrect",Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e) {
        e.printStackTrace();
        }

    }
    private class attemptLoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result="";
            String user = params[0];
            String password = params[1];

            String connstr = "http://connorlarson.ddns.net/restapi/login.php";

            try{

                URL url = new URL(connstr);
                String param = "user="+user+"&password="+password;
                Log.d(TAG, "param:" + param);

                // Open a connection using HttpURLConnection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                Log.d(TAG, "Opening conncetion");
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
        protected void onPostExecute(String Result){
            processResults(Result);
        }
    }
}
