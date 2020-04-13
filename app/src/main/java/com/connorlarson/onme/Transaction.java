package com.connorlarson.onme;

public class Transaction {
    private String transactionId;
    private String restaurantId;
    private String restaurantName;
    private String tSender;
    private String tReceiver;
    private String tAmount;
    private String tMessage;
    private String tDate;

    public Transaction(String transactionId, String restaurantId, String restaurantName, String tSender, String tReceiver, String tAmount, String tMessage, String tDate) {
        this.transactionId = transactionId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.tSender = tSender;
        this.tReceiver = tReceiver;
        this.tAmount = tAmount;
        this.tMessage = tMessage;
        this.tDate = tDate;
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
}


