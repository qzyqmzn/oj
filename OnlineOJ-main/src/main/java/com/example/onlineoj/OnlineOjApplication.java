package com.example.onlineoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.onlineoj.dao")
public class OnlineOjApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineOjApplication.class, args);
    }

}
