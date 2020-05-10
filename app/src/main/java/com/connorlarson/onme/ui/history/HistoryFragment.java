package com.connorlarson.onme.ui.history;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.R;
import com.connorlarson.onme.Transaction;
import com.connorlarson.onme.TransactionAdapter;

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

// Todo style this
public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    // data vars
    private View mView;
    private ArrayList<Transaction> transactionArray =  new ArrayList<Transaction>();
    private MainActivity activity;
    private String userId;
    private ListView transactionsListView;
    private HistoryViewModel historyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        mView = inflater.inflate(R.layout.fragment_history, container, false);

        final TextView textView = mView.findViewById(R.id.text_history);
        historyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        activity = (MainActivity) getActivity();
        userId = activity.getUserID();
        transactionsListView = mView.findViewById(R.id.transaction_history_listView);
        updateScrollViews();
        return mView;
    }
    private void updateScrollViews() {
        HistoryFragment.getTransactions GFB = new getTransactions();
        GFB.execute(userId);

    }
    private class getTransactions extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result="";
            String user = params[0];

            String connstr = "http://connorlarson.ddns.net/restapi/transactions2.php";

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
                JSONArray transactionsArray = reader.getJSONArray("Transactions");
                transactionArray.clear();
                for (int i = 0; i< transactionsArray.length(); i++){
                    JSONObject obj = transactionsArray.getJSONObject(i);
                    String tempTransId = obj.getString("TransactionId");
                    String tempResId = obj.getString("RestaurantId");
                    String tempResName = obj.getString("RestaurantName");
                    String tempSender = obj.getString("Sender");
                    String tempTransAmount = obj.getString("TransactionAmount");
                    String tempMessage = obj.getString("Message");
                    String tempDate = obj.getString("Date");
                    String tempReceiver = obj.getString("Receiver");
                    String tempRedeemed = obj.getString("Redeemed");


                    Log.d(TAG, "onPostExecute: TransId ="+ tempTransId +"  ResId="+tempResId+" ResName ="+
                            tempResName+" sender ="+tempSender+"transAmount  ="+ tempTransAmount+
                            " TransMessage ="+tempMessage+"  Date="+tempDate+" Receiver ="+tempReceiver+" Redeemed ="+tempRedeemed);
                    Transaction transaction = new Transaction(tempTransId,tempResId,tempResName,tempSender,tempReceiver,tempTransAmount,tempMessage,tempDate, Integer.parseInt(tempRedeemed));
                    transactionArray.add(transaction);
                }

                Log.d(TAG, "onPostExecute: setting adapter");
                TransactionAdapter transactionsAdapter = new TransactionAdapter(activity, transactionArray);
                transactionsListView.setAdapter(transactionsAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}