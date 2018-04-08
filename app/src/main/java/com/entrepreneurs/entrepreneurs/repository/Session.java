package com.entrepreneurs.entrepreneurs.repository;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private Map<String, Object> sessionMap;
    private static Session session;

    public static Session getSession(){
        if(null == session){
            session = new Session();
        }
        return session;
    }


    private Session(){
        sessionMap = new HashMap<>();
    }

    public Object getAttribute(String key){
        return this.sessionMap.get(key);
    }

    public void addAttribute(String key, Object value){
        this.sessionMap.put(key, value);
    }
}
