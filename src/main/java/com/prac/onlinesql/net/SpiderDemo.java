package com.prac.onlinesql.net;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Administrator
 * @Date: 2019-06-13 16:48
 * @Description:
 */
public class SpiderDemo {

    private static String SourceUrl = "http://www.shu800.com/xinggan/";

    public static void main(String[] args) throws IOException {
        readUrl(SourceUrl, "G://html//pages.txt");
        //从文件读取
        String context = readHtmlFromFile("G://html//pages.txt");
        //对html进行解析
        ArrayList<PageInfo> pages = parseHtmlToPage(context);

        pages.stream().forEach(page -> {
            try {
                ArrayList<String> imgs = parseDetailsPage(page);
                imgs.stream().forEach(img -> {
                    System.out.println("开始下载：" + page.getName());
                    downloadPic(img, page.getName());
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    private static ArrayList<String> parseDetailsPage(PageInfo page) throws MalformedURLException {
        readUrl(SourceUrl + page.getUri(), "G://html//" + page.getName() + ".txt");
        String html = readHtmlFromFile("G://html//" + page.getName() + ".txt");
        return parseHtmlToImg(html);
    }

    private static void downloadPic(String img, String direct) {
        // http://www.shu800.com/imgh/70_2602.jpg
        try {
            URL url = new URL(img);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            int index = img.lastIndexOf("/");
            String name = img.substring(index);
            File file = new File("G://file//" + direct + "//" + name);
            if(!file.exists())
                file.mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(inputStream, fos);
            fos.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static ArrayList<PageInfo> parseHtmlToPage(String context) {
        //正则匹配
        Pattern compile = Pattern.compile("<span class=\"fr\">(\\d+).*?href=\"(.*?)\".*?>(.*?)</a>");
        Matcher matcher = compile.matcher(context);
        ArrayList<PageInfo> pages = new ArrayList<>();
        while (matcher.find()){
            PageInfo page = new PageInfo();
            page.setSize(Integer.valueOf(matcher.group(1)));
            page.setUri(matcher.group(2));
            page.setName(matcher.group(3));
            pages.add(page);
        }
        return pages;
    }

    private static ArrayList<String> parseHtmlToImg(String context) {
        //正则匹配
        Pattern compile = Pattern.compile("<img.*?src=\"(.*?)\" />");
        Matcher matcher = compile.matcher(context);
        ArrayList<String> imgs = new ArrayList<>();
        while (matcher.find()){
            imgs.add(matcher.group(1));
        }
        return imgs;
    }

    private static String readHtmlFromFile(String fileName) {
        FileInputStream fis = null;
        try {
            File file = new File(fileName);
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

    private static void readUrl(String destUrl, String fileName) throws MalformedURLException {
        URL url = new URL(destUrl);
        try {
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            byte[] bytes = new byte[1024];
            //将内容写入文件
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            while(inputStream.read(bytes)!=-1){
                fos.write(bytes);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
