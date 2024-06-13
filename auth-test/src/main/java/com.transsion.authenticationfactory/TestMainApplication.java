package com.transsion.authenticationfactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/27
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TestMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestMainApplication.class, args);
    }
}
