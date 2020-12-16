package com.linxl.colibacillus.Util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class Upload {
	private static URL url;
	
	/**
	 * POST上传服务
	 * @param params  数据
	 * @param encode  编码格式
	 * @return
	 */
	public static String submitPostData(Map<String, String> params, String encode, String path) {
		
		byte[] data = null;
		try {
			data=new String(getRequestData(params, encode).toString().getBytes(),"gbk").getBytes("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	        //byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
	        try { 
	        	url = new URL(path);
	            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
	            httpURLConnection.setConnectTimeout(5000);        //设置连接超时时间
	            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
	            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
	            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
	            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
	            //设置请求体的类型是文本类型
	            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            //设置请求体的长度
	            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
	            //获得输出流，向服务器写入数据
	            OutputStream outputStream = httpURLConnection.getOutputStream();
	            outputStream.write(data);
	        	Log.d("Login", "+++++++输入数据+++++++++");
	            int response = httpURLConnection.getResponseCode();//获得服务器的响应码
	            Log.d("Login", HttpURLConnection.HTTP_OK+"+++++++服务器响应码+++++++++"+response);
	            if(response == HttpURLConnection.HTTP_OK) {
	            	Log.d("Login", "+++++++处理服务器数据+++++++++");
	                InputStream inputStream = httpURLConnection.getInputStream();
	                return dealResponseResult(inputStream);                     //处理服务器的响应结果
	            }
	        } catch (IOException e) {
	        	Log.d("Login", "+++++++链接错误+++++++++");
	            e.printStackTrace();
	        }
	        return "";
	    }
	/**
	 * 对数据进行封装
	 * @param params  数据
	 * @param encode	编码格式
	 * @return
	 */

	private static Object getRequestData(Map<String, String> params, String encode) {
		
		StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
		try {
		        for(Map.Entry<String, String> entry : params.entrySet()) {
		           stringBuffer.append(entry.getKey())
		                    .append("=")
		                    .append(URLEncoder.encode(entry.getValue(), encode))
	                        .append("&");
		         }
		         stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
		     } catch (Exception e) {
		             e.printStackTrace();
		     }
		Log.d("Login", stringBuffer.toString());
		     return stringBuffer;
		
	}
	/**
	 * 对服务器回传信息进行处理
	 * @param inptStream   服务器信息
	 * @return
	 */

	private static String dealResponseResult(InputStream inputStream) {
		String resultData = null;      //存储处理结果
		         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		         byte[] data = new byte[1024];
		         int len = 0;
		         try {
		             while((len = inputStream.read(data)) != -1) {
		                 byteArrayOutputStream.write(data, 0, len);
		             }
	         } catch (IOException e) {
		             e.printStackTrace();
		         }
		        resultData = new String(byteArrayOutputStream.toByteArray());
		         return resultData;
	}


}
