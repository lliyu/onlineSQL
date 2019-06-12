package com.prac.onlinesql.dao;

import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.util.conn.DBConnection;
import com.sun.javafx.util.Logging;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Auther: Administrator
 * @Date: 2018-11-08 15:18
 * @Description:
 */
@Component("userDao")
public class UserDao {

    public void insert()  {
        DBsQO qo = new DBsQO();
        qo.setIp("localhost");
        qo.setDbName("dn_mall_dep");
        qo.setTableName("tb_dict");
        Connection connection = DBConnection.getConnection(qo);
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO `tb_dict`(dict,type) VALUES (?, ?)");
            statement.setString(1,"Test1");
            statement.setInt(2,1);
            statement.execute();
//            int i = 1/0;
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }


    public void insertD() throws SQLException {
        DBsQO qo = new DBsQO();
        qo.setIp("localhost");
        qo.setDbName("dn_mall_dep");
        qo.setTableName("tb_dict");
        Connection connection = DBConnection.getConnection(qo);
        PreparedStatement statement = null;
        connection.setAutoCommit(true);
        statement = connection.prepareStatement("INSERT INTO `tb_dict`(dict,type) VALUES (?, ?)");
        statement.setString(1,"Test2");
        statement.setInt(2,1);
        statement.execute();
        int i = 1/0;
    }
}
