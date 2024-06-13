package com.transsion.authentication.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/10
 */
@Configuration
public class RestTemplateConfi {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
