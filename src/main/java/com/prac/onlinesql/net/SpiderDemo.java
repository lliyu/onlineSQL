package com.prac.onlinesql.net;

import com.prac.onlinesql.net.mq.entity.SubLinkPageInfo;
import com.prac.onlinesql.net.youguo.utils.FileUtils;
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

    private static String SourceUrl = "http://www.shu800.com/xinggan/index_%d.html";

    public static void main(String[] args) throws IOException {
//        for (int i = 2; i < 43; i++) {
//            System.out.println("downloading:" + i);
//            readUrl(String.format(SourceUrl, i), "G://html//pages.txt");
//            //从文件读取
//            String context = readHtmlFromFile("G://html//pages.txt");
//            ArrayList<String> imgs = parseHtmlToImg(context);
//            imgs.stream().forEach(img -> {
//                if(!"//ia.51.la/go1?id=20063721&pvFlag=1".equals(img))
//                    downloadPic(img, "pic");
//            });
//        }

        downloadPic("", "");
        //对html进行解析
//        ArrayList<PageInfo> pages = parseHtmlToPage(context);
//
//        pages.stream().forEach(page -> {
//            try {
//                ArrayList<String> imgs = parseDetailsPage(page);
//                imgs.stream().forEach(img -> {
//                    System.out.println("downloading:" + page.getName());
//                    downloadPic(img, page.getName());
//                });
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        });
    }

    private static ArrayList<String> parseDetailsPage(SubLinkPageInfo page) throws MalformedURLException {
        readUrl(SourceUrl + page.getUri(), "G://html//" + page.getName() + ".txt");
        String html = FileUtils.readHtmlFromFile("G://html//" + page.getName() + ".txt");
        return parseHtmlToImg(html);
    }

    private static void downloadPic(String img, String direct) {
        // http://www.shu800.com/imgh/70_2602.jpg
        try {
            img = "http://img.94img.com/data/0190/04/15546221346228.jpg";
//            img = "http://www.shu800.com/imgh/38_3338.jpg";
            URL url = new URL(img);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            System.out.println("avaiable:" + inputStream.available());
            int index = img.lastIndexOf("/");
            String name = img.substring(index);
            File file = new File("G://file//" + direct + "//");
            if(!file.exists())
                file.mkdirs();
            file = new File("G://file//" + direct + "//" + name);
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            long l = System.currentTimeMillis();
            IOUtils.copy(inputStream, fos);
            long l1 = System.currentTimeMillis();
            System.out.println(l1 - l);
            fos.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static ArrayList<SubLinkPageInfo> parseHtmlToPage(String context) {
        //正则匹配
        Pattern compile = Pattern.compile("<span class=\"fr\">(\\d+).*?href=\"(.*?)\".*?>(.*?)</a>");
        Matcher matcher = compile.matcher(context);
        ArrayList<SubLinkPageInfo> pages = new ArrayList<>();
        while (matcher.find()){
            SubLinkPageInfo page = new SubLinkPageInfo();
//            page.setSize(Integer.valueOf(matcher.group(1)));
            page.setUri(matcher.group(2));
            page.setName(matcher.group(3));
            pages.add(page);
        }
        return pages;
    }

    private static ArrayList<String> parseHtmlToImg(String context) {
        //正则匹配
        Pattern compile = Pattern.compile("<img.*?src=\"(.*?)\".*?>");
        Matcher matcher = compile.matcher(context);
        ArrayList<String> imgs = new ArrayList<>();
        while (matcher.find()){
            imgs.add(matcher.group(1));
        }
        return imgs;
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
