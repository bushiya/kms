package com.transsion.daconsole;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@MapperScan("com.transsion.**.module.**.mapper")
@EnableConfigurationProperties
@SpringBootApplication
public class DaConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaConsoleApplication.class, args);
    }

}
