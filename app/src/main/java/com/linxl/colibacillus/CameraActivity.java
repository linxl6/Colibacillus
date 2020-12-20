package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import com.linxl.colibacillus.model.ImagePoint;

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
        shotImageView.setMyApp(myApp);
        shotImageView.setBackground(bitmap1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shot_ok://截屏
                shotButton.setEnabled(false);
                Bitmap bitmapZ = ImageUtil.toGrayscale(shotImageView.background);
                Bitmap bitmapC = bitmapZ.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(bitmapC);
                Paint paint = new Paint();
                paint.setARGB(255,255,0,0);
                paint.setStrokeWidth(20f);//画笔宽度 （线的粗细）
                ImagePoint dot1 = shotImageView.dot1;
                ImagePoint dot2 = shotImageView.dot2;
                ImagePoint dot3 = shotImageView.dot3;
                ImagePoint dot4 = shotImageView.dot4;
                float xRatio = shotImageView.xRatio;
                float yRatio = shotImageView.yRatio;
                canvas.drawLine(dot1.x * xRatio, dot1.y * yRatio, dot2.x * xRatio, dot2.y * yRatio, paint);
                canvas.drawLine(dot2.x * xRatio, dot2.y * yRatio, dot4.x * xRatio, dot4.y * yRatio, paint);
                canvas.drawLine(dot4.x * xRatio, dot4.y * yRatio, dot3.x * xRatio, dot3.y * yRatio, paint);
                canvas.drawLine(dot3.x * xRatio, dot3.y * yRatio, dot1.x * xRatio, dot1.y * yRatio, paint);
                ImageUtil.saveImage(bitmapC, myApp.dName + ".jpg");
                Bitmap bitmap = shotImageView.getBitmap();
                if (bitmap != null) {
                    int gray = ImageUtil.calculateGrayValue(bitmap);
                    double A = gray / 20 - 1.23;
                    double count = Math.pow(10, A);
                    //Log.d("CameraActivity", "onClick: " + count);
                    myApp.vrius_count = count;
                    Intent intent = new Intent(CameraActivity.this, ResultActivity.class);
                    startActivity(intent);
                    finish();
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

}
