package com.prac.onlinesql.net.youguo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 09:37
 * @Description:
 */
public class FileUtils {

    public static String readHtmlFromFile(String fileName) {
        FileInputStream fis = null;
        try {
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();
            fis = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            byte[] bytes = new byte[1024];
            while(fis.read(bytes)!=-1){
                sb.append(new String(bytes, "utf-8"));
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
