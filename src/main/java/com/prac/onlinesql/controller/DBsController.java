package com.prac.onlinesql.controller;

import com.alibaba.druid.sql.SQLUtils;
import com.prac.onlinesql.qo.DBsQO;
import com.prac.onlinesql.qo.SelectQO;
import com.prac.onlinesql.service.DBsService;
import com.prac.onlinesql.util.result.ListResponse;
import com.prac.onlinesql.util.result.ResponseData;
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

        return new ListResponse(200, "success", dBsService.getDBs(qo), 0);
    }

    /**
     * 获取数据库表的内容信息
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/dbs/rows", method =RequestMethod.GET)
    public ListResponse rows(DBsQO qo) throws SQLException {
        long count = dBsService.queryTableTotal(qo);
        return new ListResponse(200, "success", dBsService.getRows(qo), count);
    }

    /**
     * 获取数据库表的内容信息
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/dbs/tables", method =RequestMethod.GET)
    public ListResponse tables(DBsQO qo) throws SQLException {
        return new ListResponse(200, "success", dBsService.getTables(qo), 0);
    }

    @RequestMapping(value = "/dbs/select", method =RequestMethod.GET)
    public ListResponse select(SelectQO qo) throws SQLException {
        return new ListResponse(200, "", dBsService.select(qo), 0);
    }

    @RequestMapping(value = "/dbs/format", method =RequestMethod.GET)
    public ResponseData format(String sql) {
        String res = SQLUtils.formatMySql(sql);
        return new ResponseData(res, 200, "");
    }
}
