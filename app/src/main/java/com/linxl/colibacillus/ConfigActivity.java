package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linxl.colibacillus.Util.Config;
import com.linxl.colibacillus.Util.FileUtil;
import com.linxl.colibacillus.model.ConfigItem;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * 参数配置界面
 */

public class ConfigActivity extends AppCompatActivity implements ConfigAdapter.InnerItemOnclickListener, View.OnClickListener {

    private ArrayList<ConfigItem> configList;
    private String filePath = Environment.getExternalStorageDirectory() + File.separator + Config.ConfigDir;
    private String fileName = "config.txt";//用于存储参数配置
    private ListView configItemList;
    private ConfigAdapter configAdapter;
    private Button addBtn;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        configItemList = findViewById(R.id.config_list);
        addBtn = findViewById(R.id.config_add);
        addBtn.setOnClickListener(this);
        this.initFile();
        configAdapter = new ConfigAdapter(this, configList);
        configAdapter.setOnInnerItemOnClickListener(this);
        configItemList.setAdapter(configAdapter);
        configAdapter.notifyDataSetChanged();
        myApp = (MyApp) getApplication();
    }

    //初始化系统
    private void initFile() {
        File file = new File(filePath + File.separator + fileName);
        if (file.exists()) {
            try {
                //获取解析的list
                String string = FileUtil.readPoint(filePath + File.separator + fileName);
                if (!string.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<ConfigItem>>() {
                    }.getType();
                    configList = gson.fromJson(string, userListType);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            configList = new ArrayList<>();
        }
    }


    @Override
    public void itemClick(View v) {
        int position = 0;
        position = (int) v.getTag();//位置
        Log.d("TAG", "itemClick: " + position);
        switch (v.getId()) {
            case R.id.config_edit:
                AlertDialog.Builder customizeDialog =
                        new AlertDialog.Builder(ConfigActivity.this);
                final EditText editText = new EditText(customizeDialog.getContext());
                editText.setText(configList.get(position).configName);
                customizeDialog.setTitle("参数编辑");
                customizeDialog.setView(editText);
                final int finalPosition = position;
                customizeDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 获取EditView中的输入内容
                                String string = editText.getText().toString();
                                ConfigItem item = new ConfigItem();
                                item.configName = string;
                                item.index = configList.get(finalPosition).index;
                                configList.set(finalPosition, item);
                                configAdapter.notifyDataSetChanged();
                            }
                        });
                customizeDialog.show();
                break;
            case R.id.config_delete:
                configList.remove(position);
                configAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(ConfigActivity.this);
        final EditText editText = new EditText(customizeDialog.getContext());
        customizeDialog.setTitle("参数配置");
        customizeDialog.setView(editText);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Field field = null;

                        try {
                            //通过反射获取dialog中的私有属性mShowing
                            field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);//设置该属性可以访问
                        } catch (Exception ex) {

                        }

                        // 获取EditView中的输入内容
                        String string = editText.getText().toString();
                        if (!string.equalsIgnoreCase("")) {
                            int index = (int) new Date().getTime();
                            ConfigItem item = new ConfigItem();
                            item.configName = string;
                            item.index = index;
                            configList.add(item);
                            configAdapter.notifyDataSetChanged();
                            try {
                                //设置dialog不可关闭
                                field.set(dialog, true);
                                dialog.dismiss();
                            } catch (Exception ex) {
                            }
                        } else {
                            Toast.makeText(ConfigActivity.this, "请输入正确的配置", Toast.LENGTH_SHORT).show();
                            try {
                                //设置dialog不可关闭
                                field.set(dialog, false);
                                dialog.dismiss();
                            } catch (Exception ex) {
                            }
                        }
                    }
                });
        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        customizeDialog.show();
    }

    //获取按键信息
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FileUtil.saveFile(new Gson().toJson(configList), filePath, fileName);
            myApp.configList = configList;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBack(View view) {
        finish();
    }
}
