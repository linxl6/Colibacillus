package com.linxl.colibacillus.Util;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
    public static void saveFile(String message, String filePath, String fileName) {
        byte[] bytes = message.getBytes();
        FileOutputStream fos = null;
        BufferedOutputStream buff = null;
        File file = new File(filePath, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            buff = new BufferedOutputStream(fos);
            buff.write(bytes);
            buff.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                buff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取点结果
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String readPoint(String filePath) throws IOException {
        // TODO Auto-generated method stub
        File file = new File(filePath);
        StringBuffer sb = new StringBuffer();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }
}
