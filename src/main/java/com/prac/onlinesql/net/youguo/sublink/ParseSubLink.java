package com.prac.onlinesql.net.youguo.sublink;

import com.prac.onlinesql.net.mq.entity.SubLinkPageInfo;
import com.prac.onlinesql.net.youguo.navigat.ParseNavigator;
import com.prac.onlinesql.net.youguo.utils.Constant;
import com.prac.onlinesql.net.youguo.utils.FileUtils;
import com.prac.onlinesql.net.youguo.utils.URLUtils;

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
 * @Date: 2019-06-14 09:58
 * @Description: 解析从首页获取的链接信息
 */
public class ParseSubLink {

    public static void main(String[] args) throws IOException, URISyntaxException {
        String html = FileUtils.readHtmlFromFile("G://html//img.txt");
        ParseNavigator navigator = new ParseNavigator();
        Map<String, String> links = navigator.parseNavigatorLink(html);
        Iterator<Map.Entry<String, String>> iterator = links.entrySet().iterator();
        ParseSubLink subLink = new ParseSubLink();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String content = URLUtils.readUrl(Constant.SOURCEURL + next.getValue());
            int pageSize = subLink.parseDetailPageCount(content);
            for (int i = 1; i <= pageSize; i++) {
                content = URLUtils.readUrl(URLUtils.parseURL(Constant.SOURCEURL + next.getValue(), i));
                ArrayList<SubLinkPageInfo> infos = subLink.parseHtmlToPage(content);
                System.out.println(infos);
            }
            System.out.println();
        }
    }

    public ArrayList<SubLinkPageInfo> parseHtmlToPage(String content) {
        //正则匹配
        Pattern compile = Pattern.compile("<span class=\"name\">[\\s\\S]*?<a href=\"(.*?)\">(.*?)</a>");
        Matcher matcher = compile.matcher(content);
        ArrayList<SubLinkPageInfo> pages = new ArrayList<>();
        while (matcher.find()) {
            SubLinkPageInfo page = new SubLinkPageInfo();
            page.setUri(matcher.group(1));
            page.setName(matcher.group(2).trim());
            pages.add(page);
        }
        return pages;
    }

    public int parseDetailPageCount(String html) throws MalformedURLException {
        //正则匹配
        Pattern compile = Pattern.compile("<span class=\"count\">.*?(\\d+).*?</span>");
        Matcher matcher = compile.matcher(html);
        matcher.find();
        return Integer.valueOf(matcher.group(1));
    }
}
