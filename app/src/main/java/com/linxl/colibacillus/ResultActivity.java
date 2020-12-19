package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linxl.colibacillus.Util.Config;
import com.linxl.colibacillus.Util.FileUtil;
import com.linxl.colibacillus.Util.ImageUtil;
import com.linxl.colibacillus.Util.Upload;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private TextView virusCon;
    private TextView virusName;
    private ImageView resIv;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        virusCon = findViewById(R.id.virus_con);
        virusName = findViewById(R.id.virus_name);
        resIv = findViewById(R.id.res_iv);
        myApp = (MyApp) getApplication();
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("filename");
        if (fileName != null || fileName != "") {

            try {
                myApp.vrius_count = Double.parseDouble(FileUtil.readPoint(Environment.getExternalStorageDirectory() + File.separator + Config.ResultDir + File.separator + myApp.dName + ".txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        virusCon.setText(myApp.vrius_count + "");
        virusName.setText(myApp.initName);
        String filePath = Environment.getExternalStorageDirectory() + File.separator + Config.ImageDir + File.separator + myApp.dName + ".jpg";
        Bitmap bitmap2 = ImageUtil.getBitmapWithRightRotation(filePath);
        Bitmap bitmap1 = ImageUtil.compressBitmap(bitmap2, 20);
        resIv.setImageBitmap(bitmap1);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.result_save:
                String filePath = Environment.getExternalStorageDirectory() + File.separator + Config.ResultDir;
                FileUtil.saveFile(myApp.vrius_count + "", filePath, myApp.dName + ".txt");
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.result_upload:
//                break;
        }
    }

    /**
     * 上传线程
     *
     * @author linxl
     */
    class loginThread extends Thread {
        private Map<String, String> Params;

        public loginThread(Map<String, String> params) {
            this.Params = params;
        }

        @Override
        public void run() {
//			String path ="http://115.159.68.244:80/ocean/index.php/User/phoneConfigUpload";
            String path = Config.URL + "ocean/index.php/User/phoneConfigUpload";
            Message msg = new Message();
            msg.what = 8;
            Log.d("Login", "+++++++开始上传+++++++++");
            msg.obj = Upload.submitPostData(Params, "GBK", path);
//            handler.sendMessage(msg);
            super.run();
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
