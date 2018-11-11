package com.prac.onlinesql.util.conn;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Auther: Administrator
 * @Date: 2018-11-06 09:38
 * @Description:
 */
public class DBConnection {

    private static String dirver = "com.mysql.jdbc.Driver";
//    @Value("${spring.datasource.url}")
    private static String url = "jdbc:mysql://localhost:3306/information_schema?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
//    @Value("${spring.datasource.username}")
    private static String username = "root";
//    @Value("${spring.datasource.password}")
    private static String password = "123456";

    public static Connection getConnection(){
        Connection connection = null;

        try {
            Class.forName(dirver);
            connection = DriverManager.getConnection(url,username,password);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


}
