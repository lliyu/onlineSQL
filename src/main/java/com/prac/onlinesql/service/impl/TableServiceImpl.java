package com.prac.onlinesql.service.impl;

import com.prac.onlinesql.dao.TableDao;
import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.service.TableService;
import com.prac.onlinesql.vo.ColumnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018-12-29 09:59
 * @Description:
 */
@Service("tableService")
public class TableServiceImpl implements TableService {

    @Autowired
    private TableDao tableDao;

    @Override
    public List<ColumnVO> getHeader(DBsQO qo) throws SQLException {

        return tableDao.getHeader(qo);
    }
}
