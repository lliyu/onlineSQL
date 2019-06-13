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
 * @author ly
 * @create 2019-06-13 21:46
 * 尤果爬虫
 **/
public class YouGuoPic {

    private static ArrayList<String> finishedHtml = new ArrayList<>();
    private static String SOURCEURL = "http://www.94img.com";
    private static String ALBUMS = "/albums";

    public static void main(String[] args) throws MalformedURLException {
        YouGuoPic youGuoPic = new YouGuoPic();
        String content = youGuoPic.readUrl(SOURCEURL + ALBUMS + "/YOUMI-2.html");
        ArrayList<PageInfo> pages = youGuoPic.parseHtmlToPage(content);
        pages.stream().forEach(page -> {
            try {
                int count = youGuoPic.parseDetailPageCount(page);
                for (int i = 1; i <= count; i++) {
                    if(i>1){
                        page.setUri(page.getUri().substring(0, page.getUri().lastIndexOf(".")) + "-" + i + page.getUri().substring(page.getUri().lastIndexOf(".")));;
                    }
                    if(!finishedHtml.contains(page.getUri())){
                        finishedHtml.add(page.getUri());
                        ArrayList<String> imgs = youGuoPic.parseDetailPageHtml(page);
                        imgs.stream().forEach(img -> {
                            downloadPic(img, page.getName());
                        });
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    private ArrayList<String> parseDetailPageHtml(PageInfo page) throws MalformedURLException {
        //正则匹配
        String html = readUrl(SOURCEURL + page.getUri());
        Pattern compile = Pattern.compile("<img.*?src=\"(.*?)\".*?>");
        Matcher matcher = compile.matcher(html);
        ArrayList<String> imgs = new ArrayList<>();
        while (matcher.find()) {
            imgs.add(matcher.group(1));
        }
        return imgs;
    }

    private int parseDetailPageCount(PageInfo page) throws MalformedURLException {
        //正则匹配
        String html = readUrl(SOURCEURL + page.getUri());
        Pattern compile = Pattern.compile("<span class=\"count\">.*?(\\d+).*?</span>");
        Matcher matcher = compile.matcher(html);
        matcher.find();
        return Integer.valueOf(matcher.group(1));
    }

    private static void downloadPic(String img, String direct) {
        // http://www.shu800.com/imgh/70_2602.jpg
        try {
            System.out.println("downloading:" + direct + "/" + img);
            URL url = new URL(img);
            long l = System.currentTimeMillis();
            System.out.println(l);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            int index = img.lastIndexOf("/");
            String name = img.substring(index);
            File file = new File("G://file//" + direct + "//");
            if (!file.exists())
                file.mkdirs();
            file = new File("G://file//" + direct + "//" + name);
            if (!file.exists())
                file.createNewFile();
            System.out.println(System.currentTimeMillis() - l);
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(inputStream, fos);
            fos.close();
            System.out.println(System.currentTimeMillis() - l);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<PageInfo> parseHtmlToPage(String content) {
        //正则匹配
        Pattern compile = Pattern.compile("<span class=\"name\">[\\s\\S]*?<a href=\"(.*?)\">(.*?)</a>");
        Matcher matcher = compile.matcher(content);
        ArrayList<PageInfo> pages = new ArrayList<>();
        while (matcher.find()) {
            PageInfo page = new PageInfo();
            page.setUri(matcher.group(1));
            page.setName(matcher.group(2).trim());
            pages.add(page);
        }
        return pages;
    }

    private String readUrl(String destUrl) throws MalformedURLException {
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
}
