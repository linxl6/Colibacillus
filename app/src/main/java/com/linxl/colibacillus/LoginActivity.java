package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linxl.colibacillus.Util.Config;
import com.linxl.colibacillus.Util.Upload;

import java.util.Map;

import static com.linxl.colibacillus.Util.Config.getMD5;

public class LoginActivity extends AppCompatActivity {

    private EditText ed_name;
    private EditText ed_pwd;
    private TextView tv_login;
    private MyApp myApp;
    private String name;
    private LoginThread loginThread;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /**
             * 当发送的消息是1时表示登陆成功，当发送的消息是0时表示登陆失败并显示登陆失败
             */
            if (msg.obj.toString().equalsIgnoreCase("1")) {
                //Log.d("Login", msg.obj.toString());
                myApp.userName = name;
                SharedPreferences.Editor editor = myApp.sharedPreferences.edit();
                editor.putBoolean(Config.isLogin, true);
                editor.putString(Config.UserName, name);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (msg.obj.toString().equalsIgnoreCase("0")) {
                //Log.d("Login", msg.obj.toString());
                tv_login.setText("用户名密码错误");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_name = findViewById(R.id.ed_name);
        ed_pwd = findViewById(R.id.ed_pwd);
        tv_login = findViewById(R.id.tv_login);
        myApp = (MyApp) getApplication();
    }

    public void onClick(View view) {
        tv_login.setText("");
        name = ed_name.getText().toString();
        String pwd = ed_pwd.getText().toString();
        if (name.isEmpty() || pwd.isEmpty()) {
            tv_login.setText("用户名或者密码为空");
        } else {
            Map<String, String> params = new ArrayMap<>();
            params.put("name", name);
            params.put("pwd", getMD5(pwd));
            loginThread = new LoginThread(params);
            loginThread.start();
        }
    }

    /**
     * 用于登录的线程，登陆成功，向handle发送登录消息
     */
    class LoginThread extends Thread {
        private Map<String, String> Params;

        public LoginThread(Map<String, String> params) {
            this.Params = params;
        }

        @Override
        public void run() {
            String path = Config.URL + "ocean/index.php/Login/Phonelogin";
            Message msg = new Message();
            msg.what = 8;
            msg.obj = Upload.submitPostData(Params, "GBK", path);
            handler.sendMessage(msg);
            super.run();
        }

    }
}
