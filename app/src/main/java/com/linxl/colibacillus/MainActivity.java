package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.linxl.colibacillus.Util.Config;

public class MainActivity extends AppCompatActivity {

    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        myApp = (MyApp) getApplication();

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
                break;
            case R.id.btn_change_config:
                Intent configIntent = new Intent(MainActivity.this, FileActivity.class);
                startActivity(configIntent);
                //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                finish();
                break;
            case R.id.btn_open_logic:
//                Intent logicIntent = new Intent(SelectMenu.this, LogicPhoto.class);
//                startActivity(logicIntent);
//                //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//                finish();
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
                finish();
                break;
        }
    }
}
