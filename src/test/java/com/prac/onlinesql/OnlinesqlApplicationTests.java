package com.prac.onlinesql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OnlinesqlApplicationTests {

    @Test
    public void contextLoads() {
        String connUrl = "jdbc:mysql:{0}:3306{1}?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        System.out.println(String.format(connUrl, "localhost", "test"));
        String message = MessageFormat.format(connUrl, "localhost", "test");

        System.out.println(message);
    }

}
