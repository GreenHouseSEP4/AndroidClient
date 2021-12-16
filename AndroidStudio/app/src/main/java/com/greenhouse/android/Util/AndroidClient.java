package com.greenhouse.android.Util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidClient extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        AndroidClient.context = getApplicationContext();
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Context getAppContext() {
        return AndroidClient.context;
    }
}
