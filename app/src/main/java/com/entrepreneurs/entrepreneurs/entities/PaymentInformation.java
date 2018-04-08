package com.entrepreneurs.entrepreneurs.entities;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by eshipillah on 4/5/18.
 */

public class PaymentInformation {
    private String amount;
    private String transactionID;
    private String date;

    public PaymentInformation(String transactionID, String amount, String date) {
        this.amount = amount;
        this.transactionID = transactionID;
        this.date = date;
    }

    public PaymentInformation(){
        // required for by firebase JSON to object mapping.
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getDate() {
       return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
