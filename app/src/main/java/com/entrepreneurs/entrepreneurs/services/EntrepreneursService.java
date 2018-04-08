package com.entrepreneurs.entrepreneurs.services;

import android.content.Context;

public class EntrepreneursService {
    private static EntrepreneursService entrepreneursService;

    private EntrepreneursService(Context context){
        // TODO: get the group reference from the session
        //this.group =
    }

    public static EntrepreneursService getEntrepreneursService(Context context){
        if(null == entrepreneursService){
            entrepreneursService = new EntrepreneursService(context);
        }
        return entrepreneursService;
    }


}
