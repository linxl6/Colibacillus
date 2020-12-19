package com.linxl.colibacillus;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApp extends Application {
    public SharedPreferences sharedPreferences;
    private static String ConfigName = "config";
    public String userName = "wang";//用户名
    public String dName = "";//检测的名称
    public String initName = "大肠杆菌";//细菌配置名称
    public double vrius_count = 0;//细菌浓度

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(ConfigName, MODE_PRIVATE);
    }
}
