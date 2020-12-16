package com.linxl.colibacillus;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApp extends Application {
    public SharedPreferences sharedPreferences;
    private static String ConfigName = "config";
    public String userName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(ConfigName, MODE_PRIVATE);
    }
}
