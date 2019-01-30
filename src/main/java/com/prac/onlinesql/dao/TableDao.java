package com.prac.onlinesql.dao;

import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.util.conn.DBConnection;
import com.prac.onlinesql.vo.ColumnVO;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018-12-29 09:48
 * @Description:
 */
@Component("tableDao")
public class TableDao {

    public List<ColumnVO> getHeader(DBsQO qo) throws SQLException {
        StringBuilder sql = new StringBuilder("select column_name,column_key from columns where table_name = '");
        sql.append(qo.getTableName());
        sql.append("' order by ordinal_position");
        qo.setDbName("information_schema");
        Connection connection = DBConnection.getConnection(qo);
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        ResultSet resultSet = statement.executeQuery();
        List<ColumnVO> header = new ArrayList<>();
        while (resultSet.next()){
            ColumnVO column = new ColumnVO();
            column.setColumnName(resultSet.getString("column_name"));
            column.setPrimary("PRI".equals(resultSet.getString("column_key")));
            header.add(column);
        }
        return header;
    }
}
