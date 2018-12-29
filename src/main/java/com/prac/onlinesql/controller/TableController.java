package com.prac.onlinesql.controller;

import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.service.TableService;
import com.prac.onlinesql.util.result.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

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
        return new ListResponse(0,"", tableService.getHeader(qo), 0);
    }
}
