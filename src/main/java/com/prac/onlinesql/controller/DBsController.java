package com.prac.onlinesql.controller;

import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.service.DBsService;
import com.prac.onlinesql.util.result.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * @Auther: liyu
 * @Date: 2018-11-06 09:30
 * @Description:
 */
@RestController
public class DBsController {

    @Autowired
    private DBsService dBsService;

    /**
     * 获取数据库集合
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/dbs/list", method =RequestMethod.GET)
    public ListResponse getDBs(DBsQO qo) throws SQLException {
        return new ListResponse(0, "", dBsService.getDBs(qo), 0);
    }

    /**
     * 获取数据库中表信息
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/dbs/tables", method =RequestMethod.GET)
    public ListResponse tables(DBsQO qo) throws SQLException {
        return new ListResponse(0, "",dBsService.getTables(qo), dBsService.queryTableTotal(qo));
    }
}
