package com.prac.onlinesql.service.impl;

import com.prac.onlinesql.dao.DBsDao;
import com.prac.onlinesql.entity.DBs;
import com.prac.onlinesql.mapper.TableMapper;
import com.prac.onlinesql.qo.BaseQO;
import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.qo.SelectQO;
import com.prac.onlinesql.service.DBsService;
import com.prac.onlinesql.vo.TableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: liyu
 * @Date: 2018-11-06 09:35
 * @Description:
 */
@Service("dBsService")
public class DBsServiceImpl implements DBsService {

    @Autowired
    private DBsDao dBsDao;

    @Autowired
    private TableMapper tableMapper;

    @Override
    public List<DBs> getDBs(DBsQO qo) throws SQLException {
        return dBsDao.getDBs(qo);
    }

    @Override
    public List<TableVO> getTables(DBsQO qo) throws SQLException {
//        return dBsDao.getTables(qo);
        return null;
    }

    @Override
    public List<Object> getTablesJson(DBsQO qo) {
        return dBsDao.getTables(qo);
    }

    @Override
    public List<Object> select(SelectQO qo) {
        return dBsDao.select(qo);
    }

    @Override
    public long queryTableTotal(BaseQO qo) {
        return tableMapper.queryTableTotal(qo);
    }
}
