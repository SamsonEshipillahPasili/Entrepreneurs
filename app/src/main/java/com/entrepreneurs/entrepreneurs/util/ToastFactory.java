package com.entrepreneurs.entrepreneurs.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.entrepreneurs.entrepreneurs.R;

import java.io.Serializable;

/**
 * Created by eshipillah on 4/4/18.
 */

public class ToastFactory implements Serializable{
    private Toast toast;
    private TextView toastTextView;
    private Context context;
    public enum ToastType {ERROR, INFO}

    public ToastFactory(Context context){
        this.context = context;
        this.toast = new Toast(context);
        this.toast.setDuration(Toast.LENGTH_SHORT);

        this.toastTextView = new TextView(context);
        toastTextView.setWidth(200);
        toastTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        toastTextView.setPadding(0, 10, 0, 10);
        toastTextView.setTextColor(context.getResources().getColor(R.color.white));
    }


    private Toast getErrorToast(){
        toastTextView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        toast.setView(toastTextView);
        return this.toast;
    }

    private Toast getSuccessToast(){
        toastTextView.setBackgroundColor(context.getResources().getColor(R.color.green));
        toast.setView(toastTextView);
        return this.toast;
    }

    public Toast getToast(ToastType type, String message){
        this.toastTextView.setText(message);
        switch (type){
            case ERROR:
                return this.getErrorToast();
            case INFO:
                return this.getSuccessToast();
            default:
                return this.getSuccessToast();
        }
    }
}
