package com.prac.onlinesql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.prac.onlinesql.mapper")
public class OnlinesqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinesqlApplication.class, args);
    }
}
