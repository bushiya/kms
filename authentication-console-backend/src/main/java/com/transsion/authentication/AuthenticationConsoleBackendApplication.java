package com.transsion.authentication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.transsion.authentication.model.repository.mapper")
public class AuthenticationConsoleBackendApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled", "false");
        SpringApplication.run(AuthenticationConsoleBackendApplication.class, args);
    }
}
