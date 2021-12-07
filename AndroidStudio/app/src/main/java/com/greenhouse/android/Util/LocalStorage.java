package com.greenhouse.android.Util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.greenhouse.android.View.LoginActivity;


public class LocalStorage {
    private static LocalStorage localStorage;

    public LoginActivity loginActivity;

    public LocalStorage() {
        loginActivity = new LoginActivity();
    }

    public static LocalStorage getInstance(){
        if (localStorage == null)
            localStorage = new LocalStorage();
        return  localStorage;
    }

    public void set(String key, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(loginActivity.getContextOfApplication());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(loginActivity.getContextOfApplication());
        return prefs.getString(key,"default");
    }
}
