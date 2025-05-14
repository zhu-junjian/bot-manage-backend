package com.mirrormetech.corm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mirrormetech.corm")
public class CormApplication {

    public static void main(String[] args) {
        SpringApplication.run(CormApplication.class, args);
    }

}
