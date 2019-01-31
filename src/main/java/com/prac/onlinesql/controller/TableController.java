package com.prac.onlinesql.controller;

import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.service.TableService;
import com.prac.onlinesql.util.result.ListResponse;
import com.prac.onlinesql.vo.ColumnVO;
import com.prac.onlinesql.vo.ColumnsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018-12-29 09:31
 * @Description: 查询数据表的信息
 */
@RestController
public class TableController {

    @Autowired
    private TableService tableService;

    /**
     * 获取指定表的字段信息
     * @param qo
     */
    @RequestMapping(value = "/dbs/table/header", method = RequestMethod.GET)
    public ListResponse header(DBsQO qo) throws SQLException {
        return new ListResponse(200,"", tableService.getHeader(qo), 0);
    }

    /**
     * 获取指定表的外键
     * @param qo
     */
    @RequestMapping(value = "/dbs/table/foreigns", method = RequestMethod.GET)
    public ListResponse foreigns(DBsQO qo) throws SQLException {
        return new ListResponse(200,"", tableService.getForeigns(qo), 0);
    }

    /**
     * 获取指定表的索引
     * @param qo
     */
    @RequestMapping(value = "/dbs/table/indexs", method = RequestMethod.GET)
    public ListResponse indexs(DBsQO qo) throws SQLException {
        return new ListResponse(200,"", tableService.getIndexs(qo), 0);
    }
}
