package com.prac.onlinesql.dao;

import com.alibaba.fastjson.JSONObject;
import com.prac.onlinesql.entity.DBs;
import com.prac.onlinesql.entity.Table;
import com.prac.onlinesql.qo.BaseQO;
import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.qo.SelectQO;
import com.prac.onlinesql.util.DateUtils;
import com.prac.onlinesql.util.bean.DynamicBean;
import com.prac.onlinesql.util.conn.DBConnection;
import com.prac.onlinesql.vo.TableVO;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.Date;

/**
 * @Auther: liyu
 * @Date: 2018-11-06 09:37
 * @Description:
 */
@Component("dBsDao")
public class DBsDao {

    private static Log log = LogFactory.getLog(DBsDao.class);

    public static void insertDemo() throws SQLException {
        DBsQO qo = new DBsQO();
        qo.setIp("localhost");
        qo.setDbName("fuxi");
        Connection connection = DBConnection.getConnection(qo);
        List<DBs> list = null;
        PreparedStatement statement = null;
        String sql = "insert into demo(name,age,time) values(?,?,?)";
        Random random = new Random();
        for(int i=0;i<200000;i++){
            statement = connection.prepareStatement(sql);
            statement.setString(1, "test" + random.nextInt(100));
            statement.setInt(2, random.nextInt(90));
            statement.setString(3, String.valueOf(new Date().getTime()-random.nextInt(1000)));
            boolean execute = statement.execute();
            System.out.println(execute);
        }
    }

    public static void main(String[] args) throws SQLException {
        insertDemo();
    }

    public List<DBs> getDBs(DBsQO qo) throws SQLException {
        Connection connection = DBConnection.getConnection(qo);
        List<DBs> list = null;
        PreparedStatement statement = null;
        statement = connection.prepareStatement("show databases");
        ResultSet resultSet = statement.executeQuery();
        list = new LinkedList<DBs>();
        while (resultSet.next()) {
            String db = resultSet.getString(1);
            DBs dBs = new DBs();
            dBs.setDbName(db);
            list.add(dBs);
        }
        return list;
    }

    public List<Object> getRows(DBsQO qo) throws SQLException {
        String sql = "select * from {0}";
        sql = MessageFormat.format(sql, qo.getTableName());
        StringBuilder sb = new StringBuilder(sql);
        Connection connection = DBConnection.getConnection(qo);
        if (qo.getPage() != null && qo.getLimit() != null) {
            sb.append(" limit ?,?");
        }
        List<Object> rows = new ArrayList();
        PreparedStatement statement = connection.prepareStatement(sb.toString());
        if (qo.getPage() != null && qo.getLimit() != null) {
            statement.setInt(1, (qo.getPage() - 1) * qo.getLimit());
            statement.setInt(2, qo.getLimit());
        }
        findAndPretty(rows, statement);
        return rows;
    }

    public List<Object> select(SelectQO qo) throws SQLException {
        StringBuilder sb = new StringBuilder(qo.getSql());
        Connection connection = DBConnection.getConnection(qo);
        if (qo.getPage() != null && qo.getLimit() != null) {
            sb.append("limit ?,?");
        }
        List<Object> rows = new ArrayList();
        PreparedStatement statement = connection.prepareStatement(sb.toString());
        if (qo.getPage() != null && qo.getLimit() != null) {
            statement.setInt(0, (qo.getPage() - 1) * qo.getLimit());
            statement.setInt(1, qo.getLimit());
        }
        findAndPretty(rows, statement);
        return rows;
    }

    public long queryTableTotal(DBsQO qo) throws SQLException {
        String sql = "select count(1) from {0}";
        sql = MessageFormat.format(sql, qo.getTableName());
        StringBuilder sb = new StringBuilder(sql);
        Connection connection = DBConnection.getConnection(qo);
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public List<String> getTables(DBsQO qo) throws SQLException {
        StringBuilder sb = new StringBuilder("show tables");
        Connection connection = DBConnection.getConnection(qo);
        PreparedStatement statement = connection.prepareStatement(sb.toString());
        ResultSet resultSet = statement.executeQuery();
        List<String> tables = new ArrayList<>();
        while (resultSet.next()){
            tables.add(resultSet.getString(1));
        }
        return tables;
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
                        String time = DateUtils.parseDate(rs.getTimestamp(columnName));
                        jsonObject.put(columnName, time);
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
