package com.prac.onlinesql.net.youguo.navigat;

import com.prac.onlinesql.net.youguo.utils.FileUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 09:37
 * @Description: 解析页面导航栏的链接
 */
public class ParseNavigator {
    public static void main(String[] args) {
        String html = FileUtils.readHtmlFromFile("G://html//img.txt");
        ParseNavigator navigator = new ParseNavigator();
        Map<String, String> links = navigator.parseNavigatorLink(html);
        System.out.println(links);
    }

    public Map<String, String> parseNavigatorLink(String html) {
        //正则匹配
        Pattern compile = Pattern.compile("<ul id=\"menu\">([\\s\\S]*)</ul>");
        Matcher matcher = compile.matcher(html);
        String link = null;//link表示包含所有导航链接的<ul>
        if(matcher.find()){
            link = matcher.group(1);
        }
        Map<String, String> links = new HashMap<>();
        if(!StringUtils.isEmpty(link)){
            compile = Pattern.compile("<a href=\"(.*?)\".*?title=\"(.*?)\">");
            Matcher matcher1 = compile.matcher(link);
            while(matcher1.find()){
                String uri = matcher1.group(1);
                String name = matcher1.group(2);
                links.put(name, uri);
            }
        }
        return links;
    }
}
