package com.connorlarson.onme;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Transaction> transactionArrayList;
    public TransactionAdapter(Context c, ArrayList<Transaction> transactionsArrayList){
        context = c;
        transactionArrayList = transactionsArrayList;
    }
    @Override
    public int getCount() {
        return transactionArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return transactionArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("TransactionAdapter ","getView: " );
        if(convertView == null){
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.transactions_row, parent, false);
        }
        Transaction currentItem = (Transaction) getItem(position);
        Log.d("TransactionAdapter ","getView: position" + position + " currentItem=" + currentItem.getRestaurantName());

        TextView tId = convertView.findViewById(R.id.TransactionIdTextView);
        TextView rName = convertView.findViewById(R.id.RestaurantNameTextView);
        TextView sender = convertView.findViewById(R.id.SenderTextView);
        TextView receiver = convertView.findViewById(R.id.RecieverTextView);
        TextView amount = convertView.findViewById(R.id.TransactionAmountTextView);
        TextView message = convertView.findViewById(R.id.TransactionMessageTextView);
        TextView date = convertView.findViewById(R.id.DateTextView);

        tId.setText(currentItem.getTransactionId());
        rName.setText(currentItem.getRestaurantName());
        sender.setText(currentItem.gettSender());
        receiver.setText(currentItem.gettReceiver());
        amount.setText(currentItem.gettAmount());
        message.setText(currentItem.gettMessage());
        date.setText(currentItem.gettDate());


        return convertView;
    }
}
