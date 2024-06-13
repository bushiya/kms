package com.transsion.daconsole.infrastructure.admin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "platform.admin")
public class AdminProperties {
    private String url;
    private Integer appId;
    private String appSecret;
}
