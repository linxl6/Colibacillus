package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.linxl.colibacillus.Util.Config;
import com.linxl.colibacillus.Util.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 进行拍照
 */
public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private CameraKitView cameraKitView;
    private Button cameraButton;
    private Button shotButton;
    private Button shotBack;
    private Bitmap shotImage;
    private ShotImageView shotImageView;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//        cameraKitView = findViewById(R.id.camera);
//        cameraButton = findViewById(R.id.camera_btn);
        shotButton = findViewById(R.id.shot_ok);
        shotBack = findViewById(R.id.shot_back);
        shotImageView = findViewById(R.id.shotImageView);
        myApp = (MyApp) getApplication();
//        cameraButton.setOnClickListener(this);
        shotButton.setOnClickListener(this);
//        shotBack.setOnClickListener(this);
        String filePath = Environment.getExternalStorageDirectory() + File.separator + Config.ImageDir + File.separator + myApp.dName + ".jpg";
        Bitmap bitmap2 = ImageUtil.getBitmapWithRightRotation(filePath);
        Bitmap bitmap1 = ImageUtil.compressBitmap(bitmap2, 50);
        shotImageView.setBackground(bitmap1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shot_ok://截屏
                Bitmap bitmap = shotImageView.getBitmap();
                if (bitmap != null) {
                    showImageDialog(bitmap);
                } else {
                    Toast.makeText(this, "请重新选择", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.shot_back://返回拍照
//                cameraKitView.setVisibility(View.VISIBLE);
//                shotImageView.setVisibility(View.INVISIBLE);
//                cameraButton.setVisibility(View.VISIBLE);
//                shotButton.setVisibility(View.INVISIBLE);
//                shotBack.setVisibility(View.INVISIBLE);
//                break;
        }

    }

    private void showImageDialog(final Bitmap bitmap) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CameraActivity.this);
        final ImageView imageView = new ImageView(normalDialog.getContext());
        imageView.setImageBitmap(bitmap);
        normalDialog.setView(imageView);
        normalDialog.setTitle("截取结果");
        normalDialog.setPositiveButton("确定",

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //进行灰度计算
                        int gray = ImageUtil.calculateGrayValue(bitmap);
                        double A = gray / 20 - 1.23;
                        double count = Math.pow(10, A);
                        Log.d("CameraActivity", "onClick: " + count);
                        myApp.vrius_count = count;
                        Intent intent = new Intent(CameraActivity.this, ResultActivity.class);
                        startActivity(intent);
                        //...跳转下一个页面

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //回到截取页面
                    }
                });
        // 显示
        normalDialog.show();
    }
}
