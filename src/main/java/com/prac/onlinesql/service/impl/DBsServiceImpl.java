package com.prac.onlinesql.service.impl;

import com.prac.onlinesql.dao.DBsDao;
import com.prac.onlinesql.entity.DBs;
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

    @Override
    public List<DBs> getDBs() throws SQLException {
        return dBsDao.getDBs();
    }

    @Override
    public List<TableVO> getTables() throws SQLException {
        return dBsDao.getTables();
    }
}
