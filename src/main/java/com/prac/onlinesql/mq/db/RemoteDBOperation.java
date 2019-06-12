package com.prac.onlinesql.mq.db;

import com.prac.onlinesql.mq.entity.AcademicWorksEntity;

import java.sql.*;

/**
 * @author ly
 * @create 2019-02-07 10:14
 **/
public class RemoteDBOperation {

    static Connection connection = null;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if(connection == null){
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://39.105.108.154:3306/testdb?serverTimezone=UTC", "root", "123456");
        }
        return connection;
    }

    public static boolean isTableExist(String tableName) throws SQLException, ClassNotFoundException {
        String sql = "show tables like '" + tableName + "'";
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return true;
        }
        return false;
    }

    public static void createTable(String sql) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }

    public static boolean insertData(AcademicWorksEntity entity) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String sql = "insert INTO academic_works(user_id,author,name,press_name,book_no,publish_time,print_edition,classfiy,file_no,elink,word_number,subject_classification,click_num) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, entity.getUserId());
        preparedStatement.setString(2, entity.getAuthor());
        preparedStatement.setString(3, entity.getName());
        preparedStatement.setString(4, entity.getPressName());
        preparedStatement.setString(5, entity.getBookNo());
        preparedStatement.setString(6, entity.getPublishTime());
        preparedStatement.setString(7, entity.getPrintEdition());
        preparedStatement.setString(8, entity.getClassfiy());
        preparedStatement.setString(9, entity.getFileNo());
        preparedStatement.setString(10, entity.getElink());
        preparedStatement.setDouble(11, entity.getWordNumber());
        preparedStatement.setLong(12, Long.parseLong(entity.getSubjectClassification()));
        preparedStatement.setInt(13, entity.getClickNum());

        return preparedStatement.execute();
    }

    public static void insert(String sql) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
    }
}
