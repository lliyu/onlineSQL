package com.prac.onlinesql.net.youguo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 14:40
 * @Description:
 */
public class RegexUtils {
    public static boolean matchURL(String url){
        String regex = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|][jpg|png|gif]$";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(url);
        return matcher.find();
    }

    public static void main(String[] args) {
        //http://img.94img.com/data/0190/51/15554987265257.jpg
        //http://img.v class=
        System.out.println(matchURL("http://img.v class="));
    }
}
