package com.prac.onlinesql.net.youguo.detail;

import com.prac.onlinesql.net.youguo.entity.PageInfo;
import com.prac.onlinesql.net.youguo.navigat.ParseNavigator;
import com.prac.onlinesql.net.youguo.sublink.ParseSubLink;
import com.prac.onlinesql.net.youguo.utils.Constant;
import com.prac.onlinesql.net.youguo.utils.FileUtils;
import com.prac.onlinesql.net.youguo.utils.URLUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 10:35
 * @Description: 解析详情页
 */
public class ParsePicDetail {

    private static ArrayList<String> finishedDetailHtml = new ArrayList<>();//详情页
    private static ArrayList<String> finishedLinkHtml = new ArrayList<>();//子链接

    static {
        finishedLinkHtml.add(Constant.SOURCEURL + "/");
    }

    public static void main(String[] args) throws MalformedURLException {
        String html = FileUtils.readHtmlFromFile("G://html//img.txt");
        ParseNavigator navigator = new ParseNavigator();
        Map<String, String> links = navigator.parseNavigatorLink(html);
        Iterator<Map.Entry<String, String>> iterator = links.entrySet().iterator();
        ParseSubLink subLink = new ParseSubLink();
        ParsePicDetail parsePicDetail = new ParsePicDetail();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String content = URLUtils.readUrl(Constant.SOURCEURL + next.getValue());
            int pageSize = subLink.parseDetailPageCount(content);
            for (int i = 1; i <= pageSize; i++) {
                content = URLUtils.readUrl(URLUtils.parseURL(Constant.SOURCEURL + next.getValue(), i));
                ArrayList<PageInfo> infos = subLink.parseHtmlToPage(content);
                infos.stream().forEach(page -> {
                    try {
                        int count = parsePicDetail.parseDetailPageCount(page);
                        String uri = page.getUri();
                        String uriFormat = page.getUri().substring(0, page.getUri().lastIndexOf(".")) + "-%d"+ page.getUri().substring(page.getUri().lastIndexOf("."));
                        for (int j = 1; j <= count; j++) {
                            if(j>1){
                                page.setUri(String.format(uriFormat, j));
                            }else
                                page.setUri(uri);
                            if(!finishedDetailHtml.contains(page.getUri())){
                                finishedDetailHtml.add(page.getUri());
                                ArrayList<String> imgs = parsePicDetail.parseDetailPageHtml(page);
                                imgs.stream().forEach(img -> {
                                    parsePicDetail.downloadPic(img, page.getName());
                                });
                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public ArrayList<String> parseDetailPageHtml(PageInfo page) throws MalformedURLException {
        //正则匹配
        String html = URLUtils.readUrl(Constant.SOURCEURL + page.getUri());
        Pattern compile = Pattern.compile("<img.*?src=\"(.*?)\".*?>");
        Matcher matcher = compile.matcher(html);
        ArrayList<String> imgs = new ArrayList<>();
        while (matcher.find()) {
            imgs.add(matcher.group(1));
        }
        return imgs;
    }

    public int parseDetailPageCount(PageInfo page) throws MalformedURLException {
        //正则匹配
        String html = URLUtils.readUrl(Constant.SOURCEURL + page.getUri());
        Pattern compile = Pattern.compile("<span class=\"count\">.*?(\\d+).*?</span>");
        Matcher matcher = compile.matcher(html);
        matcher.find();
        return Integer.valueOf(matcher.group(1));
    }

    public void downloadPic(String img, String direct) {
        // http://www.shu800.com/imgh/70_2602.jpg
        //将读取到的logo图片去除  正则匹配比较麻烦 所以在这里做
        if("/themes/sense/images/logo.png".equals(img))
            return;
        try {
            Thread.sleep(100);
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
//            System.out.println(System.currentTimeMillis() - l);
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(inputStream, fos);
            fos.close();
//            System.out.println(System.currentTimeMillis() - l);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
