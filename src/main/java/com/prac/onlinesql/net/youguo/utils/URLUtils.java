package com.prac.onlinesql.net.youguo.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 09:41
 * @Description: 从网络获取详细信息
 */
public class URLUtils {

    public static String readUrl(String destUrl) throws IOException, URISyntaxException {
//        URL url = new URL(destUrl);
        CloseableHttpResponse response = obtainHttpClientGet(destUrl);
        StringBuilder sb = new StringBuilder();
        try {
//            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = response.getEntity().getContent();
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1) {
                sb.append(new String(bytes, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static CloseableHttpResponse obtainHttpClientGet(String destUrl) throws IOException {
        HttpGet httpGet = new HttpGet(destUrl);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        return client.execute(httpGet);
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
