package com.linxl.colibacillus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linxl.colibacillus.Util.Config;
import com.linxl.colibacillus.Util.Load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Button loadFile;
    private ListView filelist;
    private String urlpath = Config.URL + "ocean/index.php/User/phoneConfigDownload";
    //进度条显示
    private ProgressDialog progressDialog, nameArrayDialog;
    private List<String> listiniName;
    private List<String> listiniPath;
    private ArrayAdapter<String> mAdapter;
    private MyApp myApp;
    private Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 101)  //用来获取ini的配置信息
            {
                if (msg.obj.toString().equalsIgnoreCase("获取失败")) {
                    nameArrayDialog.dismiss();
//						Log.d("msg", msg.obj.toString());
                    loadFile.setEnabled(true);
                    AlertDialog.Builder bulider = new AlertDialog.Builder(FileActivity.this);
                    bulider.setTitle("提示");
                    bulider.setMessage("获取配置文件列表失败");
                    bulider.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    });
                    bulider.create();
                    bulider.show();
                }
                if (msg.obj.toString().equalsIgnoreCase("获取成功")) {
                    Toast.makeText(FileActivity.this, "配置文件获取成功", Toast.LENGTH_SHORT).show();
                    nameArrayDialog.dismiss();
                    mAdapter.notifyDataSetChanged();
                    Log.d("msg", msg.obj.toString());
                    loadFile.setEnabled(true);
                }
            } else if (msg.what == 202)  //这里是用来下载ini文件的具体文件内容信息
            {
                if (msg.obj.toString().equalsIgnoreCase("下载成功")) {
                    progressDialog.dismiss();
                } else if (msg.obj.toString().equalsIgnoreCase("下载失败")) {
                    progressDialog.dismiss();
                }
            }
        }

        ;
    };
    private String weborFloder = "web";
    private String[] fileNameArray = null;
    private String logicpath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        myApp = (MyApp) getApplication();
        loadFile = findViewById(R.id.loadFile);
        filelist = findViewById(R.id.filename);
        filelist.setOnItemClickListener(this);
        listiniName = new ArrayList<String>();
        listiniPath = new ArrayList<String>();
        progressDialog = new ProgressDialog(FileActivity.this);
        nameArrayDialog = new ProgressDialog(FileActivity.this);
        progressDialog.setTitle("提示信息");
        nameArrayDialog.setTitle("提示信息");

        progressDialog.setMessage("正在下载中，请稍后......");
        nameArrayDialog.setMessage("正在获取中，请稍后......");
        //    设置setCancelable(false); 表示我们不能取消这个弹出框，等下载完成之后再让弹出框消失
        progressDialog.setCancelable(false);
        nameArrayDialog.setCancelable(false);
        //    设置ProgressDialog样式为圆圈的形式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //ArrayAdapter用于ListView控件与数据之间的匹配
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listiniName);
        logicpath = Environment.getExternalStorageDirectory() + Config.ConfigDir;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //点击下载按钮
            case R.id.loadFile:
                nameArrayDialog.show();
                loadFile.setEnabled(false);  //Button按钮彻底禁用
                weborFloder = "web";
                listiniName.clear();
                loadiniName mloadiniName = new loadiniName(urlpath);
                mloadiniName.start();
                filelist.setAdapter(mAdapter);  //ListView设置适配器
                break;
            case R.id.localfile:
                weborFloder = "Floder";
                if ((fileNameArray = getFolderFileName()) != null)
                    filelist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNameArray));
                fileNameArray = null;
                break;

            default:
                break;
        }
    }

    /**
     * list点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
        if (weborFloder == "web")  //当选择下载时，获取TextView中的文件信息进行文件下载
        {
            progressDialog.show();
            TextView txtfile = (TextView) v.findViewById(android.R.id.text1);
            String filename = txtfile.getText().toString() + ".ini";

//			String path = "http://115.159.68.244:80/ocean"+listiniPath.get(arg2).toString().substring(1);
            String path = Config.URL + "ocean" + listiniPath.get(arg2).toString().substring(1);

            downloadiniThread mDownloadiniThread = new downloadiniThread(path, logicpath, filename);
            mDownloadiniThread.start();
        } else if (weborFloder == "Floder")  //当选择本地文件中的某个配置文件之后，先读取他的配置文件到共享资源区，再跳转到主页面
        {
            TextView txtfile = (TextView) v.findViewById(android.R.id.text1);
            String filename = txtfile.getText().toString();
            String iniName = filename.replace(".ini", "");
            //在共享数据中设置iniName
            //save.setIniName(iniName);
            try {
                readfile(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新本地文件
     * 启用loadiniName线程来下载ini文件
     */
    class loadiniName extends Thread {
        List<HashMap<String, String>> listiniNameAndPath;
        List<HashMap<String, String>> lastiniNameAndPath;
        Message msg = new Message();
        private String uripath;

        public loadiniName(String path) {
            listiniNameAndPath = new ArrayList<HashMap<String, String>>();
            lastiniNameAndPath = new ArrayList<HashMap<String, String>>();
            msg.what = 101;
            this.uripath = path;
        }

        @Override
        public void run() {
            try {
                listiniNameAndPath = Load.getJSONObject(urlpath);
            } catch (Exception e) {
                msg.obj = "获取失败";
                mhandler.sendMessage(msg);
                e.printStackTrace();
                return;
            }
            //从获取的文件中去掉本地已经存在的文件信息，保存本地不存在的文件的信息
            lastiniNameAndPath = compareName(listiniNameAndPath, getFolderFileName());
            for (int i = 0; i < lastiniNameAndPath.size(); i++) {
//            Log.d("ininameaaaaaaaa", lastiniNameAndPath.get(i).get("iniName")+"             "+i);
                //listiniName中保存最新的ini文件
                listiniName.add(lastiniNameAndPath.get(i).get("iniName"));
                listiniPath.add(lastiniNameAndPath.get(i).get("iniPath"));
            }
            msg.obj = "获取成功";
            mhandler.sendMessage(msg);
            super.run();
        }

    }

    /**
     * 排除本地文件
     */
    private List<HashMap<String, String>> compareName(List<HashMap<String, String>> list, String[] logicname) {
        Log.d("msg", "进行比较");
        List<HashMap<String, String>> mlist = new ArrayList<HashMap<String, String>>();
        mlist = list;
        try {
            for (int i = 0; i < mlist.size(); i++) {
                for (int j = 0; j < logicname.length; j++) {
                    if (mlist.get(i).get("iniName").equalsIgnoreCase(logicname[j].replace(".ini", ""))) {
//					Log.d("sssss",(mlist.get(i).get("iniName")+"_"+mlist.get(i).get("id")));
                        mlist.remove(i);
                        i--;
                        break;

                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return mlist;

    }

    /**
     * 获取本地存储的ini文件的文件名
     *
     * @return
     */
    public String[] getFolderFileName() {
        File dir = new File(logicpath);
        if (dir.isDirectory()) {
            File[] file = dir.listFiles();
            if (file != null) {
                //String subfilelist[]=new String[file.length];
                ArrayList<String> subfilelist = new ArrayList();
                for (int i = 0; i < file.length; i++) {
                    if (file[i].getName().endsWith(".ini")) {
                        subfilelist.add(file[i].getName());
                    }
                }
                return subfilelist.toArray(new String[subfilelist.size()]);
            }
        }
        return null;
    }

    private void readfile(String filename) throws IOException {
        File file = new File(logicpath + "/" + filename);
        StringBuffer sb = new StringBuffer();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //处理数据
        getsubString(sb.toString());

//        TwoBean bean = new TwoBean();
//        bean.ss = da;
//        Intent it = new Intent(FileActivity.this, SelectMenu.class);
//        save.setBean(bean);
//        save.setColumn(Column);
//        save.setLine(Line);
//        startActivity(it);
        finish();

    }

//	private void  getsubString(String str) {
//		String[] txt=str.split("\\]");
//		Log.d("txt", txt[1]);
//		String data=txt[1].replace("[line", "");
//		data=data.replace("data=", "");
//		Log.d("txt", data);
//		String line=txt[2].substring(6, 7);
//		String column=txt[3].substring(6);
//		//Toast.makeText(this, column, Toast.LENGTH_LONG).show();
//		Line=Integer.parseInt(line);
//		Column=Integer.parseInt(column);
//		getData(data);
//	}

    /**
     * 该方法作用：从配置文件中获取需要的行、列以及细菌的数据
     *
     * @param str
     */
    private void getsubString(String str) {
//        String[] data = null;
//        try {
//            String[] txt = str.split("=");
//            Line = txt.length - 2;   //行数默认是2行，这里计算方式是txt数组的长度-2
//            Column = Integer.parseInt(txt[1].substring(0, 1));
//            data = new String[Line];  //获取细菌的类型的数据
//            for (int i = 0; i < Line; i++) {
//                data[i] = txt[i + 2];
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Line = 0;
//            Column = 0;
//            data = null;
//        }
//
//        //接下来获取细菌数据
//        getData(data);
    }

    /**
     * 新版的获取数据的方式（获取的数据是细菌数据）
     *
     * @param data
     */
    private void getData(String[] data) {
//        if (null == data || 0 == data.length) {
//            Toast.makeText(this, "配置文件中没有细菌的数据", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (0 == Line || 0 == Column) {
//            Toast.makeText(this, "获取配置文件细菌的数据失败", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        try {
//            da = new String[Line][Column];
//            String[] tmp;
//            int count = 0;
//            //装载细菌的数据
//            for (int i = 0; i < Line; i++) {
//                tmp = data[i].split(";");
//                for (int j = 0; j < Column; j++) {
//                    da[i][j] = tmp[j];
//                    Bacterial bacterial = new Bacterial(count, tmp[j]);
//                    listBacterial.add(bacterial);
//                    count++;
//                }
//            }
//            save.setListBacterial(listBacterial);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "装载细菌数据失败", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 第一版中的获取数据的方式
     *
     * @param data
     */
    @Deprecated
    private void getData(String data) {
//        da = new String[Line][Column];
//        String sstr[] = data.split("\\;");
//
//        String[] substr;
//        if (sstr != null) {
//            //substr=sstr[0].split("\\,");
//            //Toast.makeText(this, substr.length, Toast.LENGTH_LONG).show();
//            for (int i = 0; i < sstr.length; i++) {
//
//                substr = sstr[i].split("\\,");
//
//                for (int j = 0; j < Line; j++) {
//                    da[j][i] = substr[j];
//                }
//            }
//        }
        //Toast.makeText(this, da[1][1], Toast.LENGTH_LONG).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(FileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 下载配置文件
     *
     * @author linxl
     */
    class downloadiniThread extends Thread {
        private Load download;
        private String iniPath, filePath, fileName;
        private Message msg;

        public downloadiniThread(String inipath, String filepath, String filename) {
            this.iniPath = inipath;
            this.fileName = filename;
            this.filePath = filepath;
            msg = new Message();
            msg.what = 202;
        }

        @Override
        public void run() {
            try {
                Load.Load(iniPath, filePath, fileName);
            } catch (Exception e) {
                msg.obj = "下载失败";
                mhandler.sendMessage(msg);
//				Log.d("download", "下载失败");
                e.printStackTrace();
                return;
            }
            msg.obj = "下载成功";
            mhandler.sendMessage(msg);
            super.run();
        }
    }
}
