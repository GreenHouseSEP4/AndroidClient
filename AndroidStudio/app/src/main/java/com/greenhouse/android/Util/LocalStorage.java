package com.greenhouse.android.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class LocalStorage {
    private static LocalStorage localStorage;

    public Context context;

    public LocalStorage() {
        context = AndroidClient.getAppContext();
    }

    public static LocalStorage getInstance(){
        if (localStorage == null)
            localStorage = new LocalStorage();
        return  localStorage;
    }

    public void set(String key, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key,"default");
    }
}
