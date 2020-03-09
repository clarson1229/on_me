package com.connorlarson.onme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class LoginPage extends AppCompatActivity {
    EditText username;
    EditText password;
    TextView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        username = (EditText)findViewById(R.id.username_text);
        password = (EditText)findViewById(R.id.password_text);
        results = (TextView)findViewById(R.id.Results_text);
    }

    public void attemptLogIn(View view) {
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        attemptLoginTask ALT = new attemptLoginTask();

        ALT.execute(usernameString, passwordString);


//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }
    private void processResults (String response){
        String returnedData=response;

        results.setText(returnedData.toString());
    }
    private class attemptLoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result="";
            String user = params[0];
            String password = params[1];

            String connstr = "http://connorlarson.ddns.net/restapi/login.php";




            try{
                String data = URLEncoder.encode("user", "UTF-8")
                        + "=" + URLEncoder.encode(user, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(password,"UTF-8");
                URL url = new URL(connstr);
                URLConnection conn2 = url.openConnection();
                conn2.setDoOutput(true);
                conn2.setDoInput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn2.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }


                result = sb.toString();

            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//                URL url = new URL(connstr);
//                HttpURLConnection http = (HttpURLConnection) url.openConnection();
//                http.setRequestMethod("POST");
//                http.setDoInput(true);
//                http.setDoOutput(true);
//                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//                OutputStream ops = http.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
//                String data = URLEncoder.encode("user", "UTF-8")+"="+URLEncoder.encode(user,"UTF-8")
//                        +"&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
//                writer.write(data);
//                writer.flush();
//                writer.close();
//                ops.close();
//
//                http.connect();
//
//                InputStream ips = http.getInputStream();
//                BufferedReader reader = new BufferedReader((new InputStreamReader(ips,"ISO-8859-1")));
//                String line ="";
//                while((line = reader.readLine()) != null){
//                    result += line;
//                }
//                reader.close();
//                ips.close();
//                http.disconnect();

            return result;

//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                result = e.getMessage();
//            } catch (IOException e) {
//                e.printStackTrace();
//                result = e.getMessage();
//            }


        }
        @Override
        protected void onPostExecute(String Result){
            processResults(Result);
        }
    }
}
