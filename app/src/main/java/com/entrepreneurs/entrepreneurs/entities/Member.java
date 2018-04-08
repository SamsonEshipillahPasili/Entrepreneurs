package com.entrepreneurs.entrepreneurs.entities;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by eshipillah on 4/5/18.
 */

public class Member {
    private String amount;
    private String name;
    private String pin;
    private String target;
    private ArrayList<PaymentInformation> payments;

    public Member(){
        this.payments = new ArrayList<>();
    }

    public Member(String amount, String name, String pin, String target, ArrayList<PaymentInformation> payments) {
        this.amount = amount;
        this.name = name;
        this.pin = pin;
        this.target = target;
        this.payments = payments;
    }

    public String getAmount() {
        int tempAmount = 0;
        for(PaymentInformation paymentInformation : this.payments){
            try{
                tempAmount += Integer.parseInt(paymentInformation.getAmount());
            }catch(Exception e){
                Log.d("Member.getAmmount()", e.getMessage());
            }
        }
        return String.valueOf(tempAmount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<PaymentInformation> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PaymentInformation> payments) {
        this.payments = payments;
    }

    public String getPercentage(){
        int percentage = (Integer.parseInt(this.getAmount()) * 100) / Integer.parseInt(this.target);
        return String.valueOf(percentage);
    }
}
