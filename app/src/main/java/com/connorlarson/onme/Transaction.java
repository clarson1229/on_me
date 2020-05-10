package com.connorlarson.onme;

import androidx.annotation.NonNull;

public class Transaction {
    private String transactionId;
    private String restaurantId;
    private String restaurantName;
    private String tSender;
    private String tReceiver;
    private String tAmount;
    private String tMessage;
    private String tDate;
    private int redeemed;

    public Transaction(String transactionId, String restaurantId, String restaurantName, String tSender, String tReceiver, String tAmount, String tMessage, String tDate, int redeemed) {
        this.transactionId = transactionId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.tSender = tSender;
        this.tReceiver = tReceiver;
        this.tAmount = tAmount;
        this.tMessage = tMessage;
        this.tDate = tDate;
        this.redeemed = redeemed;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String gettSender() {
        return tSender;
    }

    public void settSender(String tSender) {
        this.tSender = tSender;
    }

    public String gettReceiver() {
        return tReceiver;
    }

    public void settReceiver(String tReceiver) {
        this.tReceiver = tReceiver;
    }

    public String gettAmount() {
        return tAmount;
    }

    public void settAmount(String tAmount) {
        this.tAmount = tAmount;
    }

    public String gettMessage() {
        return tMessage;
    }

    public void settMessage(String tMessage) {
        this.tMessage = tMessage;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public int getRedeemed() {
        return redeemed;
    }

    public void setRedeemed(int redeemed) {
        this.redeemed = redeemed;
    }
    @NonNull
    @Override
    public String toString() {
        return "Transaction [transactionId=" + transactionId + ", restaurantId=" + restaurantId
                + "restaurantName=" + restaurantName + "tSender=" + tSender + "tReceiver=" + tReceiver +
                "tAmount=" + tAmount + "tMessage=" + tMessage + "tDate=" + tDate + "redeemed=" + redeemed +"]";
    }
}


