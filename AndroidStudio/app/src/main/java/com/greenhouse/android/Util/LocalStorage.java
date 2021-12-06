package com.greenhouse.android.Util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.greenhouse.android.View.MainActivity;


public class LocalStorage {
    private static LocalStorage localStorage;

    public MainActivity mainActivity;

    public LocalStorage() {
        mainActivity = new MainActivity();
    }

    public static LocalStorage getInstance(){
        if (localStorage == null)
            localStorage = new LocalStorage();
        return  localStorage;
    }

    public void set(String key, String value){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity.getContextOfApplication());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity.getContextOfApplication());
        return prefs.getString(key,"default");
    }
}
