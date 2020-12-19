package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.linxl.colibacillus.Util.Config;

import java.io.File;

//本地结果
public class LogicActivity extends AppCompatActivity {

    private ListView lv_photo;
    private BaseAdapter photoAdapter;
    private String path = Environment.getExternalStorageDirectory() + File.separator + Config.ResultDir;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logic);

        lv_photo = findViewById(R.id.lv_photo);
        myApp = (MyApp) getApplication();

        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] file = dir.listFiles();
            if (file != null) {
                String subfilelist[] = new String[file.length];
                for (int i = 0; i < file.length; i++) {
                    subfilelist[i] = file[i].getName();
                }
                lv_photo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, subfilelist));
                subfilelist = null;
                lv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView txtfile = view.findViewById(android.R.id.text1);
                        String filename = txtfile.getText().toString();
                        Intent intent = new Intent(LogicActivity.this, ResultActivity.class);
                        intent.putExtra("fileName", filename.replace(".txt", ""));
                        myApp.dName = filename.replace(".txt", "");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
    }

    //获取按键信息
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(LogicActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
