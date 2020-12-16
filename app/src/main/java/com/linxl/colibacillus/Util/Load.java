package com.linxl.colibacillus.Util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Load {
	/**
	 * 获取ini文件
	 * @param Path  远程的下载路径
	 * @param filepath  本地的存储路径
	 * @param filename  要保存的文件名
	 * @throws Exception
	 */
	public static void Load(String Path, String filepath, String filename) throws Exception
	{
		URL url = new URL(Path);
		Log.d("urlpath", Path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);        //设置连接超时时间
		conn.setDoInput(true);                  //打开输入流，以便从服务器获取数据
		conn.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
		conn.setRequestMethod("GET");     //设置以Post方式提交数据
		conn.setUseCaches(false);  
		Log.d("download", "建立链接"+"                      "+conn.getResponseCode());
		if(conn.getResponseCode()==200)
		{	Log.d("download", "建立链接"+"                      "+conn.getResponseCode());
			BufferedOutputStream buf ;
			InputStream in = conn.getInputStream();
			Log.d("filepath", filepath+filename);
			File file = new File(filepath,filename);
			if(file.exists())
			{
				file.delete();
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			buf= new BufferedOutputStream(fos);
			byte[] cache = new byte[1024];
			int len =0;
			while((len=in.read(cache))!=-1)
			{
				//Log.d("load", cache.toString());
				buf.write(cache, 0, len);
			}
			Log.d("download", "开始写入");
			buf.flush();
			fos.close();
			in.close();
			buf.close();
		}
	}
	/**
	 * 用json的方式获取文件名称
	 * @param path   json路径
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<HashMap<String, String>> getJSONObject(String path)
            throws Exception {
    
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        URL url = new URL(path);
        // 利用HttpURLConnection对象，我们可以从网页中获取网页数据    
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 单位为毫秒，设置超时时间为5秒    
        conn.setConnectTimeout(30 * 1000);    
        // HttpURLConnection对象是通过HTTP协议请求path路径的，所以需要设置请求方式，可以不设置，因为默认为get    
        conn.setRequestMethod("GET");    
        if (conn.getResponseCode() == 200) {// 判断请求码是否200，否则为失败    
            InputStream is = conn.getInputStream(); // 获取输入流
            byte[] data = readStream(is); // 把输入流转换成字符串组    
            String json = new String(data); // 把字符串组转换成字符串
            // 数据形式：{arrayData":[{"iniNmae":"xxx.ini"},{"iniName":"xxx.ini"}]}    
            //JSONObject jsonObject = new JSONObject(json); // 返回的数据形式是一个Object类型，所以可以直接转换成一个Object       
            JSONArray jsonArray =  new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {    
                	JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象
            		HashMap<String, String> map = new HashMap<String, String>();
                	//Log.d("iniNameArray",item.getString("f_name")+"          "+item.getString("path"));
                	map.put("iniName", item.getString("f_name")+"_"+item.getString("id"));
                	map.put("iniPath", item.getString("path"));
                	
                	list.add(map);
                	//Log.d("ininame",map.get("iniName")+ "    "+list.get(i).get("iniName"));
            }    
        }  
    /*   for(int i=0;i<list.size();i++)
       {
    	   Log.d("aaasss",list.get(i).get("iniPath")+i);
       }*/
        return list;    
    }
	/**
	 * 输入流转换成byte【】
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	private static byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];    
        int len = 0;    
        while ((len = inputStream.read(buffer)) != -1) {    
            bout.write(buffer, 0, len);    
        }    
        bout.close();    
        inputStream.close();    
        return bout.toByteArray();    
    }
}
