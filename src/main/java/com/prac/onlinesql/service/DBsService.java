package com.prac.onlinesql.service;

import com.prac.onlinesql.entity.DBs;
import com.prac.onlinesql.qo.BaseQO;
import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.qo.SelectQO;
import com.prac.onlinesql.vo.TableVO;

import java.sql.SQLException;
import java.util.List;

public interface DBsService {
    List<DBs> getDBs(DBsQO qo) throws SQLException;

    List<TableVO> getTables(DBsQO qo) throws SQLException;
    List<Object> getTablesJson(DBsQO qo);
    List<Object> select(SelectQO qo);

    long queryTableTotal(BaseQO qo);
}
