package com.prac.onlinesql.service.impl;

import com.prac.onlinesql.dao.UserDao;
import com.prac.onlinesql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @Auther: Administrator
 * @Date: 2018-11-08 15:24
 * @Description:
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public void insert() throws Exception {
        userDao.insert();
//        userDao.insertD();
//        throw new RuntimeException();
    }
}
