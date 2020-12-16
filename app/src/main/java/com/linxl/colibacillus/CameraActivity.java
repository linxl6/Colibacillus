package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.camerakit.CameraKitView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraKitView = findViewById(R.id.camera);
        cameraButton = findViewById(R.id.camera_btn);
        shotButton = findViewById(R.id.shot_ok);
        shotBack = findViewById(R.id.shot_back);
        shotImageView = findViewById(R.id.shotImageView);
        cameraButton.setOnClickListener(this);
        shotButton.setOnClickListener(this);
        shotBack.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_btn://拍照
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView view, final byte[] capturedImage) {
                        shotImage = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
                        cameraKitView.setVisibility(View.INVISIBLE);
                        shotImageView.setVisibility(View.VISIBLE);
                        shotImageView.setBackground(shotImage);
                        cameraButton.setVisibility(View.INVISIBLE);
                        shotButton.setVisibility(View.VISIBLE);
                        shotBack.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.shot_ok://截屏
                Bitmap bitmap = shotImageView.getBitmap();
                showImageDialog(bitmap);
                break;
            case R.id.shot_back://返回拍照
                cameraKitView.setVisibility(View.VISIBLE);
                shotImageView.setVisibility(View.INVISIBLE);
                cameraButton.setVisibility(View.VISIBLE);
                shotButton.setVisibility(View.INVISIBLE);
                shotBack.setVisibility(View.INVISIBLE);
                break;
        }

    }

    private void showImageDialog(Bitmap bitmap) {
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
