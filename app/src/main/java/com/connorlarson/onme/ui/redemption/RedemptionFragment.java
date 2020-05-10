package com.connorlarson.onme.ui.redemption;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.connorlarson.onme.MainActivity;
import com.connorlarson.onme.R;
import com.connorlarson.onme.RedeemModal;
import com.connorlarson.onme.RestaurantRecyclerAdapter;
import com.connorlarson.onme.Transaction;

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

public class RedemptionFragment extends Fragment {

    private static final String TAG = "Redemption Page";
    private RecyclerView rRecyclerView;
    private RedemptionRecyclerAdapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private View mView;
    private ArrayList<Transaction> transactionArray =  new ArrayList<Transaction>();

    // data vars
    private MainActivity activity;
    private String userId;
    private static final int MODAL_REQUEST_CODE = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_redemption, container, false);

        activity = (MainActivity) getActivity();
        userId = activity.getUserID();
        init();
        return mView;
    }

    private void init() {
        // todo start here
        RedemptionFragment.getTransactions GT = new getTransactions();
        GT.execute(userId);
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
                    if (Integer.parseInt(tempRedeemed) == 0){
                        Transaction transaction = new Transaction(tempTransId,tempResId,tempResName,tempSender,tempReceiver,tempTransAmount,tempMessage,tempDate, Integer.parseInt(tempRedeemed));
                        transactionArray.add(transaction);
                    }
                }

                Log.d(TAG, "onPostExecute: setting adapter");
                if (transactionArray.size()>0){
                    buildRecyclerView();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void buildRecyclerView(){
        rRecyclerView = mView.findViewById(R.id.redemptionRecycle);
        rRecyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(activity);
        rAdapter = new RedemptionRecyclerAdapter(transactionArray);

        rRecyclerView.setLayoutManager(rLayoutManager);
        rRecyclerView.setAdapter(rAdapter);
        rAdapter.setOnItemClickListener(new RedemptionRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "itemClicked: "+transactionArray.get(position).getTransactionId());
                // todo this is where to do the item on click and start new intent
                // todo filter transaction list
                Intent i = new Intent(activity, RedeemModal.class);
                i.putExtra("USER_NAME", userId);
                i.putExtra("TRANSACTION_ID", transactionArray.get(position).getTransactionId());
                i.putExtra("RESTAURANT_NAME", transactionArray.get(position).getRestaurantName());
                i.putExtra("SENDER", transactionArray.get(position).gettSender());
                i.putExtra("TRANSACTION_AMOUNT", transactionArray.get(position).gettAmount());
                i.putExtra("MESSAGE", transactionArray.get(position).gettMessage());

                startActivityForResult(i,MODAL_REQUEST_CODE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onactivityResult: Modal_request_code=" + requestCode+ "resultCode="+ resultCode);
        init();
    }
}