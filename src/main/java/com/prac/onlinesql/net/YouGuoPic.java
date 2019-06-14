package com.prac.onlinesql.net;

import com.prac.onlinesql.net.youguo.entity.PageInfo;
import com.prac.onlinesql.net.youguo.utils.URLUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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

    public static void main(String[] args) throws IOException {
        YouGuoPic youGuoPic = new YouGuoPic();

    }

    public static void picDownNavigat() throws IOException, URISyntaxException {
        YouGuoPic youGuoPic = new YouGuoPic();
        String content = URLUtils.readUrl(SOURCEURL + ALBUMS + "/YOUMI-2.html");
        ArrayList<PageInfo> pages = youGuoPic.parseHtmlToPage(content);
        pages.stream().forEach(page -> {
            try {
                int count = youGuoPic.parseDetailPageCount(page);
                String uri = page.getUri();
                String uriFormat = page.getUri().substring(0, page.getUri().lastIndexOf(".")) + "-%d"+ page.getUri().substring(page.getUri().lastIndexOf("."));
                for (int i = 1; i <= count; i++) {
                    if(i>1){
                        page.setUri(String.format(uriFormat, i));
                    }else
                        page.setUri(uri);
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    public ArrayList<String> parseDetailPageHtml(PageInfo page) throws IOException, URISyntaxException {
        //正则匹配
        String html = URLUtils.readUrl(SOURCEURL + page.getUri());
        Pattern compile = Pattern.compile("<img.*?src=\"(.*?)\".*?>");
        Matcher matcher = compile.matcher(html);
        ArrayList<String> imgs = new ArrayList<>();
        while (matcher.find()) {
            imgs.add(matcher.group(1));
        }
        return imgs;
    }

    public int parseDetailPageCount(PageInfo page) throws IOException, URISyntaxException {
        //正则匹配
        String html = URLUtils.readUrl(SOURCEURL + page.getUri());
        Pattern compile = Pattern.compile("<span class=\"count\">.*?(\\d+).*?</span>");
        Matcher matcher = compile.matcher(html);
        matcher.find();
        return Integer.valueOf(matcher.group(1));
    }

    public static void downloadPic(String img, String direct) {
        // http://www.shu800.com/imgh/70_2602.jpg
        //将读取到的logo图片去除  正则匹配比较麻烦 所以在这里做
        if("/themes/sense/images/logo.png".equals(img))
            return;
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

    public ArrayList<PageInfo> parseHtmlToPage(String content) {
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

}
