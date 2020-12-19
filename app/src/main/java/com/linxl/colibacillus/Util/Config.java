package com.linxl.colibacillus.Util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Config {
    public static String BasicDir = "colibacillus";

    //存储结果
    public static String ResultDir = BasicDir + File.separator + "result";

    //存储图片
    public static String ImageDir = BasicDir + File.separator + "image";

    //存储配置
    public static String ConfigDir = BasicDir + File.separator + "config";

    //存储key
    public static String isLogin = "isLogin";
    public static String UserName = "name";

    public static final String URL = "http://axsw.data668.com:8080/";
    //    public static final String URL = "http://118.24.26.240:8080/";
//    public static final String URL = "http://192.168.0.130:80/";
    public static final String THRESHOLD = "threshold";

    /**
     * md5加密
     *
     * @param info 内容
     * @return MD5内容
     */
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
