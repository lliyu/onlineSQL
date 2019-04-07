package com.prac.onlinesql.mq.db;

import com.prac.onlinesql.mq.entity.AcademicWorksEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ly
 * @create 2019-02-06 22:31
 * 获取数据库中的数据
 **/
public class DBData {

    static Connection connection = null;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if(connection == null){
//            new Driver();
//            Class.forName("com.mysql.jdbc.Driver");
            ClassLoader.getSystemClassLoader().loadClass("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/renren-security?serverTimezone=UTC", "root", "123456");
        }
        return connection;
    }

    public static List<AcademicWorksEntity> getData(String tableName) throws SQLException, ClassNotFoundException {

        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from " + tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<AcademicWorksEntity> list = new ArrayList<>();
        while (resultSet.next()){
            AcademicWorksEntity entity = new AcademicWorksEntity();
            int id = resultSet.getInt("id");
            entity.setId(id);

            long user_id = resultSet.getLong("user_id");
            entity.setUserId(user_id);

            String author = resultSet.getString("author");
            entity.setAuthor(author);

            String name = resultSet.getString("name");
            entity.setName(name);

            String press_name = resultSet.getString("press_name");
            entity.setPressName(press_name);

            String book_no = resultSet.getString("book_no");
            entity.setBookNo(book_no);

            String publish_time = resultSet.getString("publish_time");
            entity.setPublishTime(publish_time);

            String print_edition = resultSet.getString("print_edition");
            entity.setPrintEdition(print_edition);

            String classify = resultSet.getString("classfiy");
            entity.setClassfiy(classify);

            String file_no = resultSet.getString("file_no");
            entity.setFileNo(file_no);

            String elink = resultSet.getString("elink");
            entity.setElink(elink);

            double word_number = resultSet.getDouble("word_number");
            entity.setWordNumber(word_number);

            long subject_classification = resultSet.getLong("subject_classification");
            entity.setSubjectClassification(String.valueOf(subject_classification));

            int click_num = resultSet.getInt("click_num");
            entity.setClickNum(click_num);

            list.add(entity);
        }

        return list;
    }

    public static String getCreateTableSQL(String tableName) throws SQLException, ClassNotFoundException {

        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("show create table " + tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder sql = new StringBuilder();
        while(resultSet.next()){
            sql.append(resultSet.getString(2));
        }

        return sql.toString();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println(getCreateTableSQL("tb_user"));
    }

}
