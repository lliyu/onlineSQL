package com.prac.onlinesql.service;

import com.prac.onlinesql.entity.DBs;
import com.prac.onlinesql.qo.BaseQO;
import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.vo.TableVO;

import java.sql.SQLException;
import java.util.List;

public interface DBsService {
    List<DBs> getDBs(DBsQO qo) throws SQLException;

    List<TableVO> getTables(DBsQO qo) throws SQLException;

    long queryTableTotal(BaseQO qo);
}
