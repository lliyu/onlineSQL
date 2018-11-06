package com.prac.onlinesql.service;

import com.prac.onlinesql.entity.DBs;

import java.sql.SQLException;
import java.util.List;

public interface DBsService {
    List<DBs> getDBs() throws SQLException;
    List<DBs> getTables() throws SQLException;
}
