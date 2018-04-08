package com.entrepreneurs.entrepreneurs.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogFactory {

    public static ProgressDialog getProgressDialog(Context context, String title, String message){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        return progressDialog;
    }
}
