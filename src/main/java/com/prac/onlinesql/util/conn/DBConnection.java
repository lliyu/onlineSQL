package com.prac.onlinesql.util.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: Administrator
 * @Date: 2018-11-06 09:38
 * @Description:
 */
public class DBConnection {

    private static Connection connection = null;
    //缓存数据库连接
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>(16);

    private static String dirver = "com.mysql.jdbc.Driver";
    //    @Value("${spring.datasource.url}")
    private static String url = "jdbc:mysql://localhost:3306/information_schema?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
    //    @Value("${spring.datasource.username}")
    private static String username = "root";
    //    @Value("${spring.datasource.password}")
    private static String password = "root";

    static {
        //默认创建一个连接
        try {
            Class.forName(dirver);
            connection = DriverManager.getConnection(url,username,password);
            connectionMap.put("information_schema", connection);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){

        try {
            Class.forName(dirver);
            connection = DriverManager.getConnection(url,username,password);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


}
