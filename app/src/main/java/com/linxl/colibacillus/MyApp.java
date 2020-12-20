package com.linxl.colibacillus;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

import com.google.gson.Gson;
import com.linxl.colibacillus.Util.Config;
import com.linxl.colibacillus.model.ConfigItem;
import com.linxl.colibacillus.model.ImagePoint;

import java.io.File;
import java.util.ArrayList;

public class MyApp extends Application {
    public SharedPreferences sharedPreferences;
    private static String ConfigName = "config";
    public String userName = "wang";//用户名
    public String dName = "";//检测的名称
    public String initName = "大肠杆菌";//细菌配置名称
    public double vrius_count = 0;//细菌浓度

    public ArrayList<ConfigItem> configList;

    public ImagePoint dot1 = new ImagePoint();
    public ImagePoint dot2 = new ImagePoint();
    public ImagePoint dot3 = new ImagePoint();
    public ImagePoint dot4 = new ImagePoint();

    public static String DOT1 = "dot1";
    public static String DOT2 = "dot2";
    public static String DOT3 = "dot3";
    public static String DOT4 = "dot4";

    private Gson gson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(ConfigName, MODE_PRIVATE);
        String dot1String = sharedPreferences.getString(DOT1, "");
        if (!dot1String.equalsIgnoreCase("")) {
            dot1 = gson.fromJson(dot1String, ImagePoint.class);
        } else {
            dot1.x = 0;  //点1 x轴坐标
            dot1.y = 0; //点1 y轴坐标
        }
        String dot2String = sharedPreferences.getString(DOT2, "");
        if (!dot2String.equalsIgnoreCase("")) {
            dot2 = gson.fromJson(dot2String, ImagePoint.class);
        } else {
            dot2.x = dot1.x + 300;
            dot2.y = dot1.y;
        }
        String dot3String = sharedPreferences.getString(DOT3, "");
        if (!dot3String.equalsIgnoreCase("")) {
            dot3 = gson.fromJson(dot3String, ImagePoint.class);
        } else {
            dot3.x = dot1.x;
            dot3.y = dot1.y + 300;
        }
        String dot4String = sharedPreferences.getString(DOT4, "");
        if (!dot4String.equalsIgnoreCase("")) {
            dot4 = gson.fromJson(dot4String, ImagePoint.class);
        } else {
            dot4.x = dot2.x;
            dot4.y = dot2.y + 300;
        }
    }
}
