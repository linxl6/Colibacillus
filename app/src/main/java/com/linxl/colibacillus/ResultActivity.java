package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linxl.colibacillus.Util.Config;
import com.linxl.colibacillus.Util.FileUtil;
import com.linxl.colibacillus.Util.ImageUtil;
import com.linxl.colibacillus.Util.Upload;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private TextView virusCon;
    private TextView virusName;
    private ImageView resIv;
    private MyApp myApp;
    private Boolean isLogic = false;
    private Button save;
    private Button del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        virusCon = findViewById(R.id.virus_con);
        virusName = findViewById(R.id.virus_name);
        resIv = findViewById(R.id.res_iv);
        myApp = (MyApp) getApplication();
        save = findViewById(R.id.result_save);
        del = findViewById(R.id.result_del);
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("fileName");
        if (fileName != null) {
            try {
                myApp.vrius_count = Double.parseDouble(FileUtil.readPoint(Environment.getExternalStorageDirectory() + File.separator + Config.ResultDir + File.separator + myApp.dName + ".txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            save.setVisibility(View.GONE);
            del.setVisibility(View.VISIBLE);
        }
        DecimalFormat df = new DecimalFormat("0.00E0");
        System.out.println(df.format(myApp.vrius_count));
        virusCon.setText(df.format(myApp.vrius_count) + "");
        virusName.setText(myApp.initName);
        String filePath = Environment.getExternalStorageDirectory() + File.separator + Config.ImageDir + File.separator + myApp.dName + ".jpg";
        Bitmap bitmap2 = ImageUtil.getBitmapWithRightRotation(filePath);
        if (bitmap2 != null) {
            Bitmap bitmap1 = ImageUtil.compressBitmap(bitmap2, 20);
            resIv.setImageBitmap(bitmap1);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.result_save:
                String filePath = Environment.getExternalStorageDirectory() + File.separator + Config.ResultDir;
                FileUtil.saveFile(myApp.vrius_count + "", filePath, myApp.dName + ".txt");
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.result_del:
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(ResultActivity.this);
                normalDialog.setTitle("警告");
                normalDialog.setMessage("是否删除这条记录?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                                String imagePath = Environment.getExternalStorageDirectory() + File.separator + Config.ImageDir;
                                String textPath = Environment.getExternalStorageDirectory() + File.separator + Config.ResultDir;
                                File imageFile = new File(imagePath, myApp.dName + ".jpg");
                                if (imageFile.exists()) {
                                    imageFile.delete();
                                }
                                File textFile = new File(textPath, myApp.dName + ".txt");
                                if (textFile.exists()) {
                                    textFile.delete();
                                }
                                Toast.makeText(ResultActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                normalDialog.setNegativeButton("关闭",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                            }
                        });
                // 显示
                normalDialog.show();
                break;
        }
    }

    public void onBack(View view) {
        finish();
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

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
