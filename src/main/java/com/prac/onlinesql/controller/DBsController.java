package com.prac.onlinesql.controller;

import com.prac.onlinesql.entity.DBs;
import com.prac.onlinesql.service.DBsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

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
    public List<DBs> getDBs() throws SQLException {
        return dBsService.getDBs();
    }

    /**
     * 获取数据库中表信息
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/dbs/tables", method =RequestMethod.GET)
    public List<DBs> tables() throws SQLException {
        return dBsService.getTables();
    }
}
