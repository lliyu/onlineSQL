package com.prac.onlinesql.net.youguo.detail;

import com.prac.onlinesql.net.mq.entity.SubLinkPageInfo;
import com.prac.onlinesql.net.youguo.navigat.ParseNavigator;
import com.prac.onlinesql.net.youguo.sublink.ParseSubLink;
import com.prac.onlinesql.net.youguo.utils.Constant;
import com.prac.onlinesql.net.youguo.utils.FileUtils;
import com.prac.onlinesql.net.youguo.utils.RegexUtils;
import com.prac.onlinesql.net.youguo.utils.URLUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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

    public static void main(String[] args) throws IOException, URISyntaxException {
        String html = FileUtils.readHtmlFromFile("G://html//img.txt");
        ParseNavigator navigator = new ParseNavigator();
        Map<String, String> links = navigator.parseNavigatorLink(html);
        Iterator<Map.Entry<String, String>> iterator = links.entrySet().iterator();
        ParseSubLink subLink = new ParseSubLink();
        ParsePicDetail parsePicDetail = new ParsePicDetail();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String rootPath = "G://file//" + next.getKey();
            File file = new File(rootPath);
            if(!file.exists()) {
                file.mkdirs();
            }
            String content = URLUtils.readUrl(Constant.SOURCEURL + next.getValue());
            int pageSize = subLink.parseDetailPageCount(content);
            for (int i = 1; i <= pageSize; i++) {
                content = URLUtils.readUrl(URLUtils.parseURL(Constant.SOURCEURL + next.getValue(), i));
                ArrayList<SubLinkPageInfo> infos = subLink.parseHtmlToPage(content);
                infos.stream().forEach(page -> {
                    try {
                        int count = parsePicDetail.parseDetailPageCount(null);
                        String uri = page.getUri();
                        String uriFormat = page.getUri().substring(0, page.getUri().lastIndexOf(".")) + "-%d"+ page.getUri().substring(page.getUri().lastIndexOf("."));
                        for (int j = 1; j <= count; j++) {
                            if(j>1){
                                page.setUri(String.format(uriFormat, j));
                            }else
                                page.setUri(uri);
                            if(!finishedDetailHtml.contains(page.getUri())){
                                finishedDetailHtml.add(page.getUri());
//                                ArrayList<String> imgs = parsePicDetail.parseDetailPageHtml(page);
//                                imgs.stream().forEach(img -> {
//                                    parsePicDetail.downloadPic(img, page.getName(), rootPath);
//                                });
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
        }
    }

    public ArrayList<String> parseDetailPageHtml(String html) throws IOException, URISyntaxException {
        //正则匹配
        Pattern compile = Pattern.compile("<img.*?src=\"(.*?)\".*?>");
        Matcher matcher = compile.matcher(html);
        ArrayList<String> imgs = new ArrayList<>();
        while (matcher.find()) {
            //这里匹配图片的url时 目前youguo可能存在某些页面中某一图片是拼凑的 正则无法直接匹配 目前先丢弃
//            String img = matcher.group(1);
//            if("http:// <div class=".equals(img))
//                continue;
            imgs.add(matcher.group(1));
        }
        return imgs;
    }

    public int parseDetailPageCount(String html) throws IOException, URISyntaxException {
        //正则匹配
        Pattern compile = Pattern.compile("<span class=\"count\">.*?(\\d+).*?");
        Matcher matcher = compile.matcher(html);
        matcher.find();
        return Integer.valueOf(matcher.group(1));
    }

    public void downloadPic(String img, String direct, String rootPath) {
        //将读取到的logo图片去除  正则匹配比较麻烦 所以在这里做
        if(!RegexUtils.matchURL(img))
            return;
        try {
            Thread.sleep(100);
            System.out.println("downloading:" + direct + "/" + img);
            CloseableHttpResponse response = URLUtils.obtainHttpClientGet(img);
            if(response.getStatusLine().getStatusCode() != 200)
                return;
            int index = img.lastIndexOf("/");
            String name = img.substring(index);
            File file = new File(rootPath + "//" + direct + "//");
            if (!file.exists())
                file.mkdirs();
            file = new File(rootPath + "//" + direct + "//" + name);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(response.getEntity().getContent(), fos);
            fos.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
