package com.prac.onlinesql.controller;


import com.prac.onlinesql.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Value("${spring.datasource.url}")
    private String url;

    @RequestMapping("/test")
    public User test(){
        User user = new User();
        user.setAge(21);
        user.setName(url);
        return user;
    }
}
