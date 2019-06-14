package com.prac.onlinesql.net.youguo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 09:41
 * @Description: 从网络获取详细信息
 */
public class URLUtils {

    public static String readUrl(String destUrl) throws MalformedURLException {
        URL url = new URL(destUrl);
        StringBuilder sb = new StringBuilder();
        try {
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1) {
                sb.append(new String(bytes, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String parseURL(String url, int page){
        if(page == 1)
            return url;
        int index = url.lastIndexOf(".");
        String perfix = url.substring(0, index);
        String suffix = url.substring(index);
        return perfix + "-" + page + suffix;
    }

    public static void main(String[] args) {
        System.out.println(parseURL(Constant.SOURCEURL + "/albums/CANDY.html", 3));
    }
}
