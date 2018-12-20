package com.prac.onlinesql.dao;

import com.prac.onlinesql.entity.DBs;
import com.prac.onlinesql.entity.Table;
import com.prac.onlinesql.qo.BaseQO;
import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.util.conn.DBConnection;
import com.prac.onlinesql.vo.TableVO;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Auther: liyu
 * @Date: 2018-11-06 09:37
 * @Description:
 */
@Component("dBsDao")
public class DBsDao {

    public List<DBs> getDBs(DBsQO qo) throws SQLException {
        Connection connection = DBConnection.getConnection(qo);
        List<DBs> list = null;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("show databases");
            ResultSet resultSet = statement.executeQuery();
            list = new LinkedList<DBs>();
            while (resultSet.next()) {
                String db = resultSet.getString(1);
                DBs dBs = new DBs();
                dBs.setDbName(db);
                list.add(dBs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null)
                statement.close();
        }
        return list;
    }

    public List getTables(DBsQO qo) {
        String sql = "select * from {0}";
        sql = MessageFormat.format(sql, qo.getTableName());
        StringBuilder sb = new StringBuilder(sql);
        Connection connection = DBConnection.getConnection(qo);
        if (qo.getPage() != null && qo.getLimit() != null) {
            sb.append("limit ?,?");
        }
        List rows = new ArrayList();
        try (PreparedStatement statement = connection.prepareStatement(sb.toString())) {
//            statement.setString(0, qo.getDbName());
            if (qo.getPage() != null && qo.getLimit() != null) {
                statement.setInt(0, (qo.getPage() - 1) * qo.getLimit());
                statement.setInt(1, qo.getLimit());
            }
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            while (rs.next()) {
                for (int j = 1; j < cols; j++) {
                    List currentRow = new ArrayList();
                    switch (metaData.getColumnType(j)) {
                        case Types.VARCHAR:
                            currentRow.add(rs.getString(metaData.getColumnName(j)));
                            break;
                        case Types.INTEGER:
                            currentRow.add(new Integer(rs.getInt(metaData.getColumnName(j))));
                            break;
                        case Types.TIMESTAMP:
                            currentRow.add(rs.getDate(metaData.getColumnName(j)));
                            break;
                        case Types.DOUBLE:
                            currentRow.add(rs.getDouble(metaData.getColumnName(j)));
                            break;
                        case Types.FLOAT:
                            currentRow.add(rs.getFloat(metaData.getColumnName(j)));
                            break;
                        case Types.CLOB:
                            currentRow.add(rs.getBlob(metaData.getColumnName(j)));
                            break;
                        default:
                            currentRow.add("error");
                    }
                    rows.add(currentRow);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }}
