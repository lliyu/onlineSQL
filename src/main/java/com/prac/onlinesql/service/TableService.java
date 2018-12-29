package com.prac.onlinesql.service;

import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.vo.ColumnVO;

import java.sql.SQLException;
import java.util.List;

/**
 * 数据表相关
 */

public interface TableService {
    List<ColumnVO> getHeader(DBsQO qo) throws SQLException;
}
