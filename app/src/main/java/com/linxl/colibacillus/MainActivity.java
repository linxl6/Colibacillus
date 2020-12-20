package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linxl.colibacillus.Util.Config;
import com.linxl.colibacillus.Util.FileUtil;
import com.linxl.colibacillus.model.ConfigItem;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyApp myApp;
    private TextView show_user_name;
    private String filePath = Environment.getExternalStorageDirectory() + File.separator + Config.ConfigDir;
    private String fileName = "config.txt";//用于存储参数配置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        myApp = (MyApp) getApplication();
        show_user_name = findViewById(R.id.show_user_name);
        show_user_name.setText("用户名：" + myApp.userName);
        initFile();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_begin:
                //表示还没有选择配置文件
//                if (save.getLine() == 0) {
//                    new AlertDialog.Builder(SelectMenu.this).setMessage("请选择配置文件").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            //这里用来处理点击确定之后的操作
////							Toast.makeText(SelectMenu.this,"没有选择配置文件",Toast.LENGTH_SHORT).show();
//
//                        }
//                    }).create().show();
//                } else {
//                    Intent cameraIntent = new Intent(SelectMenu.this, GetName.class);
//                    startActivity(cameraIntent);
//                    //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//                    finish();
//                }
                Intent cameraIntent = new Intent(MainActivity.this, PrepareActivity.class);
                startActivity(cameraIntent);
                break;
            case R.id.btn_change_config:
                Intent configIntent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(configIntent);
                //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                //finish();
                break;
            case R.id.btn_open_logic:
                Intent logicIntent = new Intent(MainActivity.this, LogicActivity.class);
                startActivity(logicIntent);
                //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                //finish();
                break;
            case R.id.btn_change_user:
                //清空SharedPreferences中的数据，并跳转回登陆界面

                SharedPreferences.Editor editor = myApp.sharedPreferences.edit();
                editor.putBoolean(Config.isLogin, false);
                editor.commit();
                Intent change_user = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(change_user);
                finish();
                break;
//            case R.id.btn_change_threshold:
//                Intent change_threshold = new Intent(SelectMenu.this, ThreSholdActivity.class);
//                startActivity(change_threshold);
//                finish();
//                break;
            case R.id.btn_logout:
//                finish();
                System.exit(0);
//                ActivityManager manager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE); //获取应用程序管理器
//                manager.killBackgroundProcesses(getPackageName());
                break;
        }
    }

    //初始化系统
    private void initFile() {
        File file = new File(filePath + File.separator + fileName);
        if (file.exists()) {
            try {
                //获取解析的list
                String string = FileUtil.readPoint(filePath + File.separator + fileName);
                if (string != "") {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<ConfigItem>>() {
                    }.getType();
                    myApp.configList = gson.fromJson(string, userListType);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            myApp.configList = new ArrayList<>();
        }
    }
}
