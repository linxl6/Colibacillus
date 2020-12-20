package com.linxl.colibacillus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.linxl.colibacillus.Util.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Future;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.linxl.colibacillus.Util.Config.BasicDir;
import static com.linxl.colibacillus.Util.Config.ConfigDir;
import static com.linxl.colibacillus.Util.Config.ImageDir;
import static com.linxl.colibacillus.Util.Config.ResultDir;

public class WelcomeActivity extends AppCompatActivity {

    private MyApp myApp;
    private WelcomeThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        myApp = (MyApp) getApplication();
        thread = new WelcomeThread(myApp.sharedPreferences.getBoolean(Config.isLogin, true));
        createFileFord();
        applePermission();
    }

    private class WelcomeThread extends Thread {

        private Boolean isLogin;

        public WelcomeThread(Boolean is) {
            isLogin = is;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isLogin) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            super.run();
        }

    }

    private void applePermission() {

        ArrayList<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.INTERNET);
        }
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
        } else {
            thread.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        thread.start();
    }

    private void createFileFord() {//生成存储文件夹
        // TODO Auto-generated method stub
        String path = Environment.getExternalStorageDirectory() + File.separator;
        File dir = new File(path+BasicDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File f = new File(path+ImageDir);
        if (!f.exists()) {
            f.mkdir();
        }
        File file = new File(path+ResultDir);
        if (!file.exists()) {
            file.mkdir();
        }
        File file1 = new File(path+ConfigDir);
        if (!file1.exists()) {
            file1.mkdir();
        }
    }
}
