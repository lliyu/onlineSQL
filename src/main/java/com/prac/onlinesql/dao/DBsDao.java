package com.prac.onlinesql.dao;

import com.prac.onlinesql.entity.DBs;
import com.prac.onlinesql.entity.Table;
import com.prac.onlinesql.util.conn.DBConnection;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Auther: liyu
 * @Date: 2018-11-06 09:37
 * @Description:
 */
@Component("dBsDao")
public class DBsDao {

    public List<DBs> getDBs() throws SQLException {
        Connection connection = DBConnection.getConnection();
        List<DBs> list = null;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("show databases");
            ResultSet resultSet = statement.executeQuery();
            list = new LinkedList<DBs>();
            while(resultSet.next()){
                String db = resultSet.getString(1);
                DBs dBs = new DBs();
                dBs.setDbName(db);
                list.add(dBs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection != null)
                connection.close();
            if(statement != null)
                statement.close();
        }
        return list;
    }

    public List<DBs> getTables() throws SQLException {
        List<DBs> dBs = getDBs();
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = null;
        for(DBs db:dBs){
            try {
                statement = connection.prepareStatement("select `table_name`,`table_type`,`engine`,`table_rows` from tables where `table_schema` = ?");
                statement.setString(1, db.getDbName());
                ResultSet resultSet = statement.executeQuery();
                List<Table> tables = new LinkedList<>();
                while(resultSet.next()){
                    String tableName = resultSet.getString(1);
                    String tableType = resultSet.getString(2);
                    String engine = resultSet.getString(3);
                    int rows = resultSet.getInt(4);
                    Table table = new Table();
                    table.setTableName(tableName);
                    table.setEngine(engine);
                    table.setRows(rows);
                    table.setTableType(tableType);
                    tables.add(table);
                }
                db.setTables(tables);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null)
            connection.close();
        if(statement != null)
            statement.close();
        return dBs;
    }
}
