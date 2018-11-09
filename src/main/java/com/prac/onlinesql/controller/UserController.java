package com.prac.onlinesql.controller;


import com.prac.onlinesql.entity.User;
import com.prac.onlinesql.service.UserService;
import com.prac.onlinesql.util.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;


@RestController
public class UserController {

    @Value("${spring.datasource.url}")
    private String url;

    @Autowired
    private UserService userService;

    @RequestMapping("/test")
    public ResponseData test() throws Exception {
        userService.insert();
        return new ResponseData(1,"test");
    }
}
