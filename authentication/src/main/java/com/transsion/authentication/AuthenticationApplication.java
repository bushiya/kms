package com.transsion.authentication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.transsion.**.module.**.mapper")
@SpringBootApplication
public class AuthenticationApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(AuthenticationApplication.class, args);
    }

}
