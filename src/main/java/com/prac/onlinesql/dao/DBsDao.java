package com.prac.onlinesql.dao;

import com.alibaba.fastjson.JSONObject;
import com.prac.onlinesql.entity.DBs;
import com.prac.onlinesql.entity.Table;
import com.prac.onlinesql.qo.BaseQO;
import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.qo.SelectQO;
import com.prac.onlinesql.util.bean.DynamicBean;
import com.prac.onlinesql.util.conn.DBConnection;
import com.prac.onlinesql.vo.TableVO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

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

    public List<Object> getTables(DBsQO qo) {
        String sql = "select * from {0}";
        sql = MessageFormat.format(sql, qo.getTableName());
        StringBuilder sb = new StringBuilder(sql);
        Connection connection = DBConnection.getConnection(qo);
        if (qo.getPage() != null && qo.getLimit() != null) {
            sb.append(" limit ?,?");
        }
        List<Object> rows = new ArrayList();
        try (PreparedStatement statement = connection.prepareStatement(sb.toString())) {
            if (qo.getPage() != null && qo.getLimit() != null) {
                statement.setInt(1, (qo.getPage() - 1) * qo.getLimit());
                statement.setInt(2, qo.getLimit());
            }
            findAndPretty(rows, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public List<Object> select(SelectQO qo){
        StringBuilder sb = new StringBuilder(qo.getSql());
        Connection connection = DBConnection.getConnection(qo);
        if (qo.getPage() != null && qo.getLimit() != null) {
            sb.append("limit ?,?");
        }
        List<Object> rows = new ArrayList();
        try (PreparedStatement statement = connection.prepareStatement(sb.toString())) {
            if (qo.getPage() != null && qo.getLimit() != null) {
                statement.setInt(0, (qo.getPage() - 1) * qo.getLimit());
                statement.setInt(1, qo.getLimit());
            }
            findAndPretty(rows, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private void findAndPretty(List<Object> rows, PreparedStatement statement) throws SQLException {
        //execute如果返回了结果集 则返回的值为true 表示select操作
        //如果返回为false 则表示ddl操作
        HashMap propertyMap = new HashMap();
        boolean result = statement.execute();
        ResultSet rs = null;
        if(result){
            rs = statement.getResultSet();
        }else {
            return;
        }
        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();
        JSONObject jsonObject = null;
        while (rs.next()) {
            jsonObject = new JSONObject();
            for (int j = 1; j <= cols; j++) {
                String columnName = metaData.getColumnName(j);
                switch (metaData.getColumnType(j)) {
                    case Types.BIT:
                    case Types.BOOLEAN:
                        jsonObject.put(columnName, rs.getBoolean(columnName));
                        break;
                    case Types.VARCHAR:
                    case Types.LONGNVARCHAR:
                    case Types.CHAR:
                        jsonObject.put(columnName, rs.getString(columnName));
                        break;
                    case Types.INTEGER:
                    case Types.TINYINT:
                    case Types.SMALLINT:
                        jsonObject.put(columnName, rs.getInt(columnName));
                        break;
                    case Types.TIMESTAMP:
                        jsonObject.put(columnName, rs.getTimestamp(columnName));
                        break;
                    case Types.TIME:
                        jsonObject.put(columnName, rs.getTime(columnName));
                        break;
                    case Types.BINARY:
                    case Types.VARBINARY:
                    case Types.LONGVARBINARY:
                        jsonObject.put(columnName, rs.getBytes(columnName));
                        break;
                    case Types.DOUBLE:
                        jsonObject.put(columnName, rs.getDouble(columnName));
                        break;
                    case Types.FLOAT:
                    case Types.REAL:
                        jsonObject.put(columnName, rs.getFloat(columnName));
                        break;
                    case Types.BIGINT:
                        jsonObject.put(columnName, rs.getLong(columnName));
                        break;
                    case Types.NUMERIC:
                    case Types.DECIMAL:
                        jsonObject.put(columnName, rs.getBigDecimal(columnName));
                        break;
                    case Types.CLOB:
                        jsonObject.put(columnName, rs.getBlob(columnName));
                        break;
                    case Types.NULL:
                        jsonObject.put(columnName, rs.getString(columnName));
                        break;
                    case Types.REF:
                        jsonObject.put(columnName, rs.getRef(columnName));
                        break;
                    case Types.OTHER:
                    case Types.JAVA_OBJECT:
                        jsonObject.put(columnName, rs.getObject(columnName));
                        break;
                }
            }
            rows.add(jsonObject);
        }
    }
}
